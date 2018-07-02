package flavor.pie.armorstandtools

import com.flowpowered.math.vector.Vector3d
import flavor.pie.kludge.*
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.ArmorStand
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.type.Exclude
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.text.chat.ChatTypes
import java.util.Optional

object ToolListeners {

    @Listener
    fun onUseSummoner(e: InteractItemEvent.Secondary, @First p: Player) {
        p[CarryingKeys.CARRYING].get().unwrap().let {
            if (it != null) {
                e.isCancelled = true
                p[CarryingKeys.CARRYING] = Optional.empty()
                p.sendMessage(ChatTypes.ACTION_BAR, !"Dropped armor stand.")
                return@onUseSummoner
            }
        }
        val toolType = e.itemStack[ToolKeys.TOOL_TYPE].unwrap()
        if (!p.hasPermission(Permissions.USE) && !toolType.isNone()) {
            p.sendMessage(ChatTypes.ACTION_BAR, "You don't have permission to use tools.".red())
            e.isCancelled = true
            return
        }
        if (toolType == ToolType.SUMMON && p.hasPermission("astools.summon")) {
            if (!p.hasPermission(Permissions.SUMMON)) {
                p.sendMessage(ChatTypes.ACTION_BAR, "You don't have permission to use the summoner.".red())
            }
            e.isCancelled = true
            val transform = MathUtils.getTransformFacing(p.transform)
            val stand = p.world.createEntity(EntityTypes.ARMOR_STAND, transform.position) as ArmorStand
            stand.transform = transform
            stand.setHelmet(ArmorStandTools.config.defaultGear.helmet?.createStack())
            stand.setChestplate(ArmorStandTools.config.defaultGear.chestplate?.createStack())
            stand.setLeggings(ArmorStandTools.config.defaultGear.leggings?.createStack())
            stand.setBoots(ArmorStandTools.config.defaultGear.boots?.createStack())
            stand.setItemInHand(HandTypes.MAIN_HAND, ArmorStandTools.config.defaultGear.mainHand?.createStack())
            stand.setItemInHand(HandTypes.OFF_HAND, ArmorStandTools.config.defaultGear.offHand?.createStack())
            stand[Keys.ARMOR_STAND_HAS_ARMS] = ArmorStandTools.config.defaultSettings.arms
            stand[Keys.ARMOR_STAND_HAS_BASE_PLATE] = ArmorStandTools.config.defaultSettings.basePlate
            stand[Keys.HAS_GRAVITY] = ArmorStandTools.config.defaultSettings.gravity
            stand[Keys.INVULNERABLE] = ArmorStandTools.config.defaultSettings.invulnerable
            stand[Keys.ARMOR_STAND_IS_SMALL] = ArmorStandTools.config.defaultSettings.small
            stand[Keys.INVISIBLE] = !ArmorStandTools.config.defaultSettings.visible
            ArmorStandTools.config.defaultSettings.name?.let { stand[Keys.DISPLAY_NAME] = it }
            if (p.world.spawnEntity(stand)) {
                p.sendMessage(ChatTypes.ACTION_BAR, !"Carrying armor stand. Click to drop.")
                p[CarryingKeys.CARRYING] = stand.uniqueId.optional()
                p[CarryingKeys.ORIGINAL_LOCATION] = Optional.empty()
            } else {
                p.sendMessage(ChatTypes.ACTION_BAR, !"Could not spawn armor stand.")
            }
            return
        }
    }

    @Listener
    fun onUseToolOnBlock(e: InteractBlockEvent.Secondary, @First p: Player) {
        if (p[CarryingKeys.CARRYING].get().isPresent) {
            return
        }
        val tool = p.getItemInHand(e.handType).unwrap()?.get(ToolKeys.TOOL_TYPE).unwrap()
        if (!p.hasPermission(Permissions.USE) && !tool.isNone()) {
            p.sendMessage(ChatTypes.ACTION_BAR, "You don't have permission to use this tool.".red())
            e.isCancelled = true
            return
        }
        if (!tool.isNone() && tool != ToolType.SUMMON) {
            e.isCancelled = true
        }
    }

    @Listener(order = Order.LATE)
    @Exclude(MoveEntityEvent.Teleport::class)
    fun onMove(e: MoveEntityEvent, @Getter("getTargetEntity") p: Player) {
        val carried = p[CarryingKeys.CARRYING].get().unwrap() ?: return
        val entity = p.world.getEntity(carried).unwrap()
        if (entity == null) {
            p[CarryingKeys.CARRYING] = Optional.empty()
            return
        }
        entity.transform = MathUtils.getTransformFacing(e.toTransform)
        p.sendMessage(ChatTypes.ACTION_BAR, !"Carrying armor stand. Click to drop.")
    }

    @Listener
    fun onTeleport(e: MoveEntityEvent.Teleport, @Getter("getTargetEntity") p: Player) {
        if (e.fromTransform.extent == e.toTransform.extent) {
            return
        }
        val carried = p[CarryingKeys.CARRYING].get().unwrap() ?: return
        val entity = e.fromTransform.extent.getEntity(carried).unwrap()
        if (entity == null) {
            p[CarryingKeys.CARRYING] = Optional.empty()
            return
        }
        val original = p[CarryingKeys.ORIGINAL_LOCATION].get().unwrap()
        if (original == null) {
            entity.remove()
        } else {
            entity.transform = original
        }
    }

    @Listener
    fun modifyEntity(e: InteractEntityEvent.Secondary, @First p: Player) {
        val item = p.getItemInHand(e.handType).unwrap() ?: return
        val tool = item[ToolKeys.TOOL_TYPE].unwrap()
        if (tool.isNone()) {
            return
        }
        e.isCancelled = true
        val stand = e.targetEntity as? ArmorStand ?: return
        val angle = e.interactionPoint.unwrap()?.y?.let {
            var x = it - stand.location.y
            if (x < 0) {
                x = 0.0
            }
            if (x > 2) {
                x = 2.0
            }
            x = 2 - x
            x * Math.PI
        }?.let(Math::toDegrees)

        if (angle == null && tool in ToolType.REQUIRES_ANGLE) {
            return
        }
        if (!p.hasPermission(Permissions.USE)) {
            p.sendMessage(ChatTypes.ACTION_BAR, "You don't have permission to use this tool.".red())
            e.isCancelled = true
            return
        }
        when (tool) {
            ToolType.SUMMON -> return
            ToolType.GUI -> {
                p.openInventory(InventoryUtils.makeInv(stand, p))
            }
            ToolType.MOVE_X -> stand.location = stand.location.add(if (p[Keys.IS_SNEAKING].get()) { -0.1 } else { 0.1 }, 0.0, 0.0)
            ToolType.MOVE_Y -> stand.location = stand.location.add(0.0, if (p[Keys.IS_SNEAKING].get()) { -0.1 } else { 0.1 }, 0.0)
            ToolType.MOVE_Z -> stand.location = stand.location.add(0.0, 0.0, if (p[Keys.IS_SNEAKING].get()) { -0.1 } else { 0.1 })
            ToolType.ROTATION -> stand.rotation = stand.rotation.run { Vector3d(x, angle!!, z) }
            ToolType.HEAD_X -> stand += stand.getValue(Keys.HEAD_ROTATION).get().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.HEAD_Y -> stand += stand.getValue(Keys.HEAD_ROTATION).get().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.HEAD_Z -> stand += stand.getValue(Keys.HEAD_ROTATION).get().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            ToolType.BODY_X -> stand += stand.bodyPartRotationalData.bodyRotation().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.BODY_Y -> stand += stand.bodyPartRotationalData.bodyRotation().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.BODY_Z -> stand += stand.bodyPartRotationalData.bodyRotation().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            ToolType.LEFT_LEG_X -> stand += stand.getValue(Keys.LEFT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.LEFT_LEG_Y -> stand += stand.getValue(Keys.LEFT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.LEFT_LEG_Z -> stand += stand.getValue(Keys.LEFT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            ToolType.RIGHT_LEG_X -> stand += stand.getValue(Keys.RIGHT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.RIGHT_LEG_Y -> stand += stand.getValue(Keys.RIGHT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.RIGHT_LEG_Z -> stand += stand.getValue(Keys.RIGHT_LEG_ROTATION).get().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            ToolType.LEFT_ARM_X -> stand += stand.getValue(Keys.LEFT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.LEFT_ARM_Y -> stand += stand.getValue(Keys.LEFT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.LEFT_ARM_Z -> stand += stand.getValue(Keys.LEFT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            ToolType.RIGHT_ARM_X -> stand += stand.getValue(Keys.RIGHT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(angle!!, y, z) }) }
            ToolType.RIGHT_ARM_Y -> stand += stand.getValue(Keys.RIGHT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(x, angle!!, z) }) }
            ToolType.RIGHT_ARM_Z -> stand += stand.getValue(Keys.RIGHT_ARM_ROTATION).get().let { it.set(it.get().run { Vector3d(x, y, angle!!) }) }
            else -> {}
        }
    }

    @Listener
    fun leftClick(e: InteractItemEvent.Primary, @First p: Player) {
        if (p[SavedInventoryKeys.HAS_TOOLS].orElse(false) && !p.getItemInHand(HandTypes.MAIN_HAND).unwrap()?.get(ToolKeys.TOOL_TYPE).unwrap().isNone()) {
            for (i in 0 until 9) {
                val temp = p.storageInventory.grid(18 + i)
                p.storageInventory.grid(9 + i)?.let { p.storageInventory.grid[18 + i] = it.copy() } ?: p.storageInventory.grid[18 + i]?.clear()
                p.storageInventory.grid(i)?.let { p.storageInventory.grid[9 + i] = it.copy() } ?: p.storageInventory.grid[9 + i]?.clear()
                p.storageInventory.hotbar(i)?.let { p.storageInventory.grid[i] = it.copy() } ?: p.storageInventory.grid[i]?.clear()
                temp?.let { p.storageInventory.hotbar[i] = it.copy() } ?: p.storageInventory.hotbar[i]?.clear()
            }
            e.isCancelled = true
        }
    }

    @Listener
    fun quit(e: ClientConnectionEvent.Disconnect) {
        val p = e.targetEntity
        val carried = p[CarryingKeys.CARRYING].get().unwrap() ?: return
        val entity = p.world.getEntity(carried).unwrap()
        if (entity == null) {
            p[CarryingKeys.CARRYING] = Optional.empty()
            p[CarryingKeys.ORIGINAL_LOCATION] = Optional.empty()
            return
        }
        val original = p[CarryingKeys.ORIGINAL_LOCATION].get().unwrap()
        if (original == null) {
            entity.remove()
        } else {
            entity.transform = original
        }
        p[CarryingKeys.CARRYING] = Optional.empty()
        p[CarryingKeys.ORIGINAL_LOCATION] = Optional.empty()
    }
}
