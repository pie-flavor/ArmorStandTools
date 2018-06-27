package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player

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
        CommandManager.register(plugin, astools, "astools", "ast")
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
}