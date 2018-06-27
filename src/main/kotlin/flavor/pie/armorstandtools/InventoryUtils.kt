package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.data.Property
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.data.type.SkullTypes
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.ArmorStand
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.InventoryArchetypes
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.property.StringProperty
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import java.util.UUID

object InventoryUtils {

//    val openInventories: Multimap<UUID, UUID> = HashMultimap.create()

    fun removeTools(p: Player) {
        p.inventory.query<Inventory>(*Tools.all().map { QueryOperationTypes.ITEM_STACK_IGNORE_QUANTITY.of(it) }.toTypedArray()).clear()
    }

    fun restoreInventory(p: Player) {
        val currentInv = p.storageInventory.slots.mapNotNull { it.peek().unwrap() }
        p.inventory.clear()
        val savedHotbar = p[SavedInventoryKeys.HOTBAR].get()
        val savedInv = p[SavedInventoryKeys.INVENTORY].get()
        val inv = p.storageInventory.grid
        val hotbar = p.storageInventory.hotbar
        for (i in 0 until savedInv.size) {
            savedInv[i]?.let { inv[i] = it.createStack() }
        }
        for (i in 0 until savedHotbar.size) {
            savedHotbar[i]?.let { hotbar[i] = it.createStack() }
        }
        for (item in currentInv) {
            p.storageInventory.offer(item).rejectedItems.forEach {
                val entity = p.world.createEntity(EntityTypes.ITEM, p.location.position)
                entity[Keys.REPRESENTED_ITEM] = it
                p.world.spawnEntity(entity)
            }
        }
        p[SavedInventoryKeys.HAS_TOOLS] = false
    }

    fun giveTools(p: Player) {
        p.storageInventory.clear()
        val hotbar = p.storageInventory.hotbar
        hotbar[0] = Tools.summon.copy()
        hotbar[1] = Tools.gui.copy()
        hotbar[2] = Tools.rotation.copy()
        hotbar[3] = Tools.moveX.copy()
        hotbar[4] = Tools.moveY.copy()
        hotbar[5] = Tools.moveZ.copy()
        val grid = p.storageInventory.grid
        grid[0, 0] = Tools.bodyX.copy()
        grid[1, 0] = Tools.bodyY.copy()
        grid[2, 0] = Tools.bodyZ.copy()
        grid[3, 0] = Tools.headX.copy()
        grid[4, 0] = Tools.headY.copy()
        grid[5, 0] = Tools.headZ.copy()
        grid[0, 1] = Tools.leftLegX.copy()
        grid[1, 1] = Tools.leftLegY.copy()
        grid[2, 1] = Tools.leftLegZ.copy()
        grid[3, 1] = Tools.rightLegX.copy()
        grid[4, 1] = Tools.rightLegY.copy()
        grid[5, 1] = Tools.rightLegZ.copy()
        grid[0, 2] = Tools.leftArmX.copy()
        grid[1, 2] = Tools.leftArmY.copy()
        grid[2, 2] = Tools.leftArmZ.copy()
        grid[3, 2] = Tools.rightArmX.copy()
        grid[4, 2] = Tools.rightArmY.copy()
        grid[5, 2] = Tools.rightArmZ.copy()
    }

    fun saveInventory(p: Player) {
        val inv = p.storageInventory.grid.slots.mapNotNull { it.peek().unwrap()?.createSnapshot() }.withIndex().map { it.index to it.value }.toMap()
        val hotbar = p.storageInventory.hotbar.slots.mapNotNull { it.peek().unwrap()?.createSnapshot() }.withIndex().map { it.index to it.value }.toMap()
        p[SavedInventoryKeys.HOTBAR] = hotbar
        p[SavedInventoryKeys.INVENTORY] = inv
        p[SavedInventoryKeys.HAS_TOOLS] = true
    }

    fun makeInv(stand: ArmorStand, p: Player): Inventory {
        val inv = inventoryOf(ArmorStandTools.instance) {
            of(InventoryArchetypes.CHEST)
            property(InventoryDimension.of(9, 4))
            property(InventoryTitle.of(stand[Keys.DISPLAY_NAME].unwrap() ?: !stand.translation))
            property(Identifiable(stand.uniqueId, Property.Operator.EQUAL))
        }
        updateInv(stand, inv, p)
        return inv
    }

    fun updateInv(stand: ArmorStand, inv: Inventory, p: Player) {
        val empty = itemStackOf {
            itemType(ItemTypes.STAINED_GLASS)
            add(Keys.DYE_COLOR, DyeColors.GRAY)
            add(Keys.DISPLAY_NAME, !"")
        }
        for (i in arrayOf(0, 1, 3, 4, 8, 9, 13, /* SC#1959 15, */ 17, 18, 19, 21, 22, 26, 27, 28, 30, 31, 34, 35)) {
            inv[i] = empty.copy()
        }
        stand.helmet.unwrap()?.let { inv[2] = it.copy() }
        stand.chestplate.unwrap()?.let { inv[11] = it.copy() }
        stand.leggings.unwrap()?.let { inv[20] = it.copy() }
        stand.boots.unwrap()?.let { inv[29] = it.copy() }
        stand.getItemInHand(HandTypes.MAIN_HAND).unwrap()?.let { inv[10] = it.copy() }
        stand.getItemInHand(HandTypes.OFF_HAND).unwrap()?.let { inv[12] = it.copy() }
        inv[5] = itemStackOf {
            itemType(ItemTypes.NAME_TAG)
            add(Keys.DISPLAY_NAME, "Change Name".yellow())
            add(Keys.ITEM_LORE, listOf("Currently: ".aqua() + (stand[Keys.DISPLAY_NAME].unwrap()?.blue() ?: "None".blue())))
        }
        inv[6] = itemStackOf {
            itemType(ItemTypes.SKULL)
            add(Keys.SKULL_TYPE, SkullTypes.PLAYER)
            add(Keys.DISPLAY_NAME, "Give Player Head".yellow())
            add(Keys.ITEM_LORE, if (p.hasPermission(Permissions.HEAD)) {
                listOf("Currently: ".aqua() +
                        (stand.helmet.unwrap().let {
                            if (it?.type == ItemTypes.SKULL) {
                                it?.get(Keys.REPRESENTED_PLAYER).unwrap()
                            } else {
                                null
                            } }?.name.unwrap()?.green() ?: "None".blue()))
            } else {
                listOf("You don't have permission for this.".red())
            })
        }
        inv[7] = itemStackOf {
            itemType(ItemTypes.GOLD_NUGGET)
            add(Keys.DISPLAY_NAME, "Toggle Visibility".yellow())
            add(Keys.ITEM_LORE, listOf("Visible: ".aqua() + if (stand[Keys.INVISIBLE].orElse(false)) "True".green() else "False".red()))
        }
        inv[14] = itemStackOf {
            itemType(ItemTypes.FEATHER)
            add(Keys.DISPLAY_NAME, "Pick Up (Move)".yellow())
            add(Keys.ITEM_LORE, listOf("Pick up this armor stand to move it".green()))
        }
        inv[15] = itemStackOf {
            itemType(ItemTypes.GLOWSTONE_DUST)
            add(Keys.DISPLAY_NAME, "Clone".yellow())
            add(Keys.ITEM_LORE, if (p.hasPermission(Permissions.CLONE)) {
                listOf("Clone this armor stand and pick it up".green())
            } else {
                listOf("You don't have permission for this.".red())
            })

        }
        // SC#1959
        inv[16] = itemStackOf {
            itemType(ItemTypes.DIAMOND)
            add(Keys.DISPLAY_NAME, "Create Command Block".yellow())
            add(Keys.ITEM_LORE, if (p.hasPermission(Permissions.CMDBLOCK)) {
                listOf("Create command block to summon this armor stand".green())
            } else {
                listOf("You don't have permission for this.".red())
            })
        }
        inv[23] = itemStackOf {
            itemType(ItemTypes.ARROW)
            add(Keys.DISPLAY_NAME, "Toggle Arms".yellow())
            add(Keys.ITEM_LORE, listOf("Arms: ".aqua() + if (stand[Keys.ARMOR_STAND_HAS_ARMS].orElse(true)) "On".green() else "Off".red()))
        }
        inv[24] = itemStackOf {
            itemType(ItemTypes.BOOK)
            add(Keys.DISPLAY_NAME, "Toggle Base".yellow())
            add(Keys.ITEM_LORE, listOf("Base plate: ".aqua() + if (stand[Keys.ARMOR_STAND_HAS_BASE_PLATE].orElse(true)) "On".green() else "Off".red()))
        }
        inv[25] = itemStackOf {
            itemType(ItemTypes.EMERALD)
            add(Keys.DISPLAY_NAME, "Toggle Size".yellow())
            add(Keys.ITEM_LORE, listOf("Size: ".aqua() + if (stand[Keys.ARMOR_STAND_IS_SMALL].orElse(false)) "Small".blue() else "Normal".green()))
        }
        inv[32] = itemStackOf {
            itemType(ItemTypes.GHAST_TEAR)
            add(Keys.DISPLAY_NAME, "Toggle Gravity".yellow())
            add(Keys.ITEM_LORE, listOf("Gravity: ".aqua() + if (stand[Keys.HAS_GRAVITY].orElse(true)) "On".green() else "Off".red()))
        }
        inv[33] = itemStackOf {
            itemType(ItemTypes.GOLDEN_CARROT)
            add(Keys.DISPLAY_NAME, "Toggle Invulnerability".yellow())
            add(Keys.ITEM_LORE, listOf("Invulnerability: ".aqua() + if (stand[Keys.INVULNERABLE].orElse(false)) "On".green() else "Off".red()))
        }
    }
}


fun Player.toggleTools(): Boolean {
    return if (this[SavedInventoryKeys.HAS_TOOLS].get()) {
        InventoryUtils.removeTools(this)
        InventoryUtils.restoreInventory(this)
        false
    } else {
        InventoryUtils.saveInventory(this)
        InventoryUtils.giveTools(this)
        true
    }
}

fun ArmorStand.getActiveInventories(): List<Pair<Player, Inventory>> {
    val invs = mutableListOf<Pair<Player, Inventory>>()
    for (player in Server.onlinePlayers) {
        val inv = player.openInventory.unwrap() ?: continue
        if (uniqueId == inv.uniqueId) {
            invs.add(player to inv)
        }
    }
    return invs
}

fun ArmorStand.updateAllInventories() {
    taskOf(ArmorStandTools.instance) {
        execute { _ ->
            for ((p, inv) in getActiveInventories()) {
                InventoryUtils.updateInv(this@updateAllInventories, inv, p)
            }
        }
        delayTicks(1)
    }

}

fun Inventory.getHolder(): Player? {
    var parent = parent()
    while (true) {
        val newParent = parent()
        if (parent == newParent) {
            break
        } else {
            parent = newParent
        }
    }
    for (player in Server.onlinePlayers) {
        if (player.openInventory.unwrap() == parent) {
            return player
        }
    }
    return null
}

fun Inventory.getArmorStand(): ArmorStand? {
    val id = parent().uniqueId ?: return null
    for (player in Server.onlinePlayers) {
        val inv = player.openInventory.unwrap() ?: continue
        if (inv.uniqueId == id) {
            return player.world.getEntity(id).unwrap() as? ArmorStand?
        }
    }
    return null
}

val Inventory.uniqueId: UUID? get() =
    this.getProperty(Identifiable::class.java, Identifiable.getDefaultKey(Identifiable::class.java))
            .unwrap()?.value
val Inventory.slotIndex: Int? get() =
    this.getProperty(SlotIndex::class.java, SlotIndex.getDefaultKey(SlotIndex::class.java)).unwrap()?.value
val Inventory.stringProperty: String? get() =
    this.getProperty(StringProperty::class.java, StringProperty.getDefaultKey(StringProperty::class.java)).unwrap()?.value

fun Player.closeInventoryLater() {
    taskOf(ArmorStandTools.instance) {
        execute { _ -> closeInventory() }
    }
}
