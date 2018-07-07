package flavor.pie.armorstandtools

import com.flowpowered.math.vector.Vector3d
import com.google.common.collect.Lists
import flavor.pie.kludge.*
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.ArmorStand
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Locatable

@Suppress("UNUSED_PARAMETER")
object Commands {

    internal fun register(plugin: ArmorStandTools) {
        val reload = commandSpecOf {
            executor(::reload)
            description(!"Reload the config")
            permission(Permissions.RELOAD)
        }
        val astools = commandSpecOf {
            executor(::astools)
            description(!"Give yourself the armor stand tools")
            permission(Permissions.COMMAND)
            child(reload, "reload")
        }
        val assignConsole = commandSpecOf {
            executor(::assignConsole)
            description(!"Assign a command to be run by the console")
            permission(Permissions.COMMAND_ASSIGN_CONSOLE)
            arguments(GenericArguments.optional(GenericArguments.entity(!"stand", EntityTypes.ARMOR_STAND)),
                    GenericArguments.remainingJoinedStrings(!"command"))
        }
        val assignPlayer = commandSpecOf {
            executor(::assignPlayer)
            description(!"Assign a command to be run by the player")
            permission(Permissions.COMMAND_ASSIGN_PLAYER)
            arguments(GenericArguments.optional(GenericArguments.entity(!"stand", EntityTypes.ARMOR_STAND)),
                    GenericArguments.remainingJoinedStrings(!"command"))
        }
        val assign = commandSpecOf {
            child(assignConsole, "console")
            child(assignPlayer, "player")
        }
        val remove = commandSpecOf {
            executor(::remove)
            description(!"Remove commands from an armor stand")
            permission(Permissions.COMMAND_REMOVE)
            arguments(GenericArguments.optional(GenericArguments.entity(!"stand", EntityTypes.ARMOR_STAND)),
                    GenericArguments.optional(GenericArguments.integer(!"index")))
        }
        val view = commandSpecOf {
            executor(::view)
            description(!"View an armor stand's commands")
            permission(Permissions.COMMAND_VIEW)
            arguments(GenericArguments.optional(GenericArguments.onlyOne(
                    GenericArguments.entity(!"stand", EntityTypes.ARMOR_STAND))))
        }
        val ascmd = commandSpecOf {
            child(assign, "assign")
            child(remove, "remove")
            child(view, "view")
        }
        CommandManager.register(plugin, astools, "astools", "ast")
        CommandManager.register(plugin, ascmd, "ascmd")
    }

    private fun astools(src: CommandSource, args: CommandContext): CommandResult {
        if (src !is Player) {
            throw CommandException(!"You must be a player!")
        }
        if (src.toggleTools()) {
            src.sendMessage("Given armor stand tools. L-click any tool to cycle through tools.".green())
            src.sendMessage("Run this command again to return your inventory.".blue())
        } else {
            src.sendMessage("Inventory contents returned".green())
        }
        return CommandResult.success()
    }

    private fun reload(src: CommandSource, args: CommandContext): CommandResult {
        ArmorStandTools.instance.loadConfig()
        return CommandResult.success()
    }

    private fun assignConsole(src: CommandSource, args: CommandContext): CommandResult {
        val stands = args.getAll<ArmorStand>("stand")
        val command = args.getOne<String>("command").get()
        return assignCommands(src, stands, "*$command")
    }

    private fun assignPlayer(src: CommandSource, args: CommandContext): CommandResult {
        val stands = args.getAll<ArmorStand>("stand")
        val command = args.getOne<String>("command").get()
        return assignCommands(src, stands, command)
    }

    private fun assignCommands(src: CommandSource, stands: Collection<ArmorStand>, command: String): CommandResult {
        return getSpecifiedStands(src, stands) {
            assignCommand(it, command)
        }
    }

    private fun assignCommand(stand: ArmorStand, command: String) {
        val data = stand.getOrCreate(StoredCommandData::class.java).get()
        val list = Lists.newArrayList(data.commands)
        list += command
        data.commands = list
        stand += data
    }

    private fun getSpecifiedStands(src: CommandSource, stands: Collection<ArmorStand>, function: (ArmorStand) -> Unit)
            : CommandResult {
        return if (stands.isEmpty()) {
            if (src !is Locatable) {
                throw CommandException(!"No armor stands specified!")
            }
            val stand = getClosestStand(src)
            function(stand)
            CommandResult.success()
        } else {
            for (stand in stands) {
                function(stand)
            }
            CommandResult.affectedEntities(stands.size)
        }
    }

    private fun getClosestStand(locatable: Locatable): ArmorStand {
        val list = locatable.location.extent.entities
                .filter { it.location.position - locatable.location.position < Vector3d(4.0, 4.0, 4.0) }
                .mapNotNull { it as? ArmorStand }
                .sortedBy { it.location.position }
        if (list.isEmpty()) {
            throw CommandException(!"No armor stands specified and none found nearby!")
        }
        return list[0]
    }

    private fun remove(src: CommandSource, args: CommandContext): CommandResult {
        val index = args.getOne<Int>("index").unwrap()
        return getSpecifiedStands(src, args.getAll<ArmorStand>("stand")) { stand ->
            val data = stand.get(StoredCommandData::class.java).unwrap() ?: return@getSpecifiedStands
            if (index != null) {
                val list = Lists.newArrayList(data.commands)
                list.removeAt(index)
                data.commands = list
            } else {
                data.commands = emptyList()
            }
        }
    }

    private fun view(src: CommandSource, args: CommandContext): CommandResult {
        val stand = args.getOne<ArmorStand>("stand").unwrap() ?: if (src is Locatable) {
            getClosestStand(src)
        } else {
            throw CommandException(!"No armor stand specified and none found nearby!")
        }
        val commands = stand.get(StoredCommandData::class.java).unwrap()?.commands ?: emptyList()
        paginationListOf {
            val name = stand[Keys.DISPLAY_NAME].unwrap() ?: Text.EMPTY
            if (name.isEmpty) {
                title(!"Commands")
            } else {
                title(!"Commands on " + name)
            }
            contents(commands.map { if (it.startsWith("*")) {
                !"(console) ${it.substring(1)}"
            } else {
                !"(player) $it"
            } })
        }.sendTo(src)
        return commandResultOf {
            successCount(1)
            queryResult(commands.size)
        }
    }
}
