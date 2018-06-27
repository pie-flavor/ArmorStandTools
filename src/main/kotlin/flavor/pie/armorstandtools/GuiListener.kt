package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.type.SkullTypes
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.text.Text
import java.util.Optional

@Suppress("NAME_SHADOWING")
object GuiListener {

    @Listener
    fun onClick(e: ClickInventoryEvent, @First p: Player) {
        val (slot, stand) = e.transactions.map { it.slot.getArmorStand()?.let { v -> it.slot to v } }
                .firstOrNull() ?: return
        if (!p.hasPermission(Permissions.USE)) {
            p.closeInventoryLater()
            e.isCancelled = true
            return
        }
        when (slot.slotIndex) {
            2 -> stand.setHelmet(slot.peek().unwrap())
            10 -> stand.setItemInHand(HandTypes.MAIN_HAND, slot.peek().unwrap())
            11 -> stand.setChestplate(slot.peek().unwrap())
            12 -> stand.setItemInHand(HandTypes.OFF_HAND, slot.peek().unwrap())
            20 -> stand.setLeggings(slot.peek().unwrap())
            29 -> stand.setBoots(slot.peek().unwrap())

            5 -> {
                p.closeInventoryLater()
                p.sendMessage("Type the new name of the armor stand".green())
                val pid = p.uniqueId
                val sid = stand.uniqueId
                ArmorStandTools.instance.whenPlayerNextSpeaks(p) {
                    val p = Server.getPlayer(pid).unwrap() ?: return@whenPlayerNextSpeaks
                    val stand = p.world.getEntity(sid).unwrap() ?: return@whenPlayerNextSpeaks
                    if (it == "\\") {
                        stand[Keys.DISPLAY_NAME] = Text.EMPTY
                        stand[Keys.CUSTOM_NAME_VISIBLE] = false
                    } else {
                        stand[Keys.DISPLAY_NAME] = it.textByFormat()
                        stand[Keys.CUSTOM_NAME_VISIBLE] = true
                    }
                }
                e.isCancelled = true
            }
            6 -> {
                e.isCancelled = true
                if (!p.hasPermission(Permissions.HEAD)) {
                    return
                }
                p.closeInventoryLater()
                p.sendMessage("Type the name of the player to get the skull of".green())
                val pid = p.uniqueId
                ArmorStandTools.instance.whenPlayerNextSpeaks(p) {
                    GameProfileManager[it].whenComplete { t, u ->
                        val p = Server.getPlayer(pid).unwrap() ?: return@whenComplete
                        if (u != null) {
                            p.sendMessage("Could not retrieve profile".red())
                            return@whenComplete
                        }
                        val stack = itemStackOf {
                            itemType(ItemTypes.SKULL)
                            add(Keys.SKULL_TYPE, SkullTypes.PLAYER)
                            add(Keys.REPRESENTED_PLAYER, t)
                        }
                        p.inventory += stack
                    }
                }
            }
            7 -> {
                stand[Keys.INVISIBLE] = !stand[Keys.INVISIBLE].orElse(false)
                e.isCancelled = true
            }
            14 -> {
                p.closeInventoryLater()
                p[CarryingKeys.ORIGINAL_LOCATION] = stand.transform.optional()
                p[CarryingKeys.CARRYING] = stand.uniqueId.optional()
                e.isCancelled = true
                return
            }
            15 -> {
                e.isCancelled = true
                if (!p.hasPermission(Permissions.CLONE)) {
                    return
                }
                p.closeInventoryLater()
                val entity = entityArchetypeOf {
                    type(EntityTypes.ARMOR_STAND)
                    entityData(stand.toRawContainer())
                }.toSnapshot(stand.location).restore().unwrap()
                if (entity != null) {
                    p[CarryingKeys.ORIGINAL_LOCATION] = Optional.empty()
                    p[CarryingKeys.CARRYING] = entity.uniqueId.optional()
                } else {
                    p.sendMessage("Error spawning entity".red())
                }
                return
            }
            // SC#1959
            16 -> {
                e.isCancelled = true
                if (!p.hasPermission(Permissions.CMDBLOCK)) {
                    return
                }
                val command = "summon armor_stand ~ ~1 ~" + DataFormats.JSON.write(stand.getVanillaData())
                val stack = createCommandBlock(command)
                p.inventory += stack
            }
            23 -> {
                stand[Keys.ARMOR_STAND_HAS_ARMS] = !stand[Keys.ARMOR_STAND_HAS_ARMS].orElse(false)
                e.isCancelled = true
            }
            24 -> {
                stand[Keys.ARMOR_STAND_HAS_BASE_PLATE] = !stand[Keys.ARMOR_STAND_HAS_BASE_PLATE].orElse(true)
                e.isCancelled = true
            }
            25 -> {
                stand[Keys.ARMOR_STAND_IS_SMALL] = !stand[Keys.ARMOR_STAND_IS_SMALL].orElse(false)
                e.isCancelled = true
            }
            32 -> {
                stand[Keys.HAS_GRAVITY] = !stand[Keys.HAS_GRAVITY].orElse(true)
                e.isCancelled = true
            }
            33 -> {
                stand[Keys.INVULNERABLE] = !stand[Keys.INVULNERABLE].orElse(false)
                e.isCancelled = true
            }
            in 0 until 36 -> {
                e.isCancelled = true
                return
            }
        }
        stand.updateAllInventories()
    }

}
