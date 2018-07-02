package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.entity.living.ArmorStand
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.data.Has

object InteractionListener {

    @Listener
    fun interact(e: InteractEntityEvent.Secondary, @First p: Player,
                 @Getter("getTargetEntity") @Has(StoredCommandData::class) stand: ArmorStand) {
        if (p.getItemInHand(HandTypes.MAIN_HAND).unwrap()?.get(ToolKeys.TOOL_TYPE).unwrap().isNone()
                && p.getItemInHand(HandTypes.OFF_HAND).unwrap()?.get(ToolKeys.TOOL_TYPE).unwrap().isNone()
                && !p[Keys.IS_SNEAKING].get()
                && p.hasPermission(Permissions.COMMAND_EXECUTE)) {
            val data = stand[StoredCommandData::class.java].get()
            if (!data.commands.isEmpty()) {
                e.isCancelled = true
                for (command in data.commands) {
                    if (command.startsWith('*')) {
                        CommandManager.process(Server.console, command.substring(1))
                    } else {
                        CommandManager.process(p, command)
                    }
                }
            }
        }
    }

}