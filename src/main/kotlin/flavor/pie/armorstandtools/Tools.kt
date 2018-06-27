package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.text.Text

object Tools {
    val summon = makeTool(ItemTypes.ARMOR_STAND, ToolType.SUMMON, "Summon Armor Stand",
            "R-Click to summon an armor stand and pick it up", "Any click will drop it")
    val gui = makeTool(ItemTypes.NETHER_STAR, ToolType.GUI, "GUI Multi-Tool",
            "R-Click armor stand to open a GUI to tweak the attributes of the armor stand or change its armor and items")
    val rotation = makeTool(ItemTypes.MAGMA_CREAM, ToolType.ROTATION, "R-Click armor stand to change its rotation",
            "Value depends on how high up the body you click")
    val moveX = makeTool(ItemTypes.SHEARS, ToolType.MOVE_X, "R-Click armor stand to move +0.1 X",
            "Crouch R-Click for -0.1 X")
    val moveY = makeTool(ItemTypes.SHEARS, ToolType.MOVE_Y, "R-Click armor stand to move +0.1 Y",
            "Crouch R-Click for -0.1 Y")
    val moveZ = makeTool(ItemTypes.SHEARS, ToolType.MOVE_Z, "R-Click armor stand to move +0.1 Z",
            "Crouch R-Click for -0.1 Z")
    val leftArmX = makeTool(ItemTypes.TORCH, ToolType.LEFT_ARM_X, "R-Click armor stand to change left arm X value",
            "Value depends on how high up the body you click")
    val leftArmY = makeTool(ItemTypes.TORCH, ToolType.LEFT_ARM_Y, "R-Click armor stand to change left arm Y value",
            "Value depends on how high up the body you click")
    val leftArmZ = makeTool(ItemTypes.TORCH, ToolType.LEFT_ARM_Z, "R-Click armor stand to change left arm Z value",
            "Value depends on how high up the body you click")
    val leftLegX = makeTool(ItemTypes.BONE, ToolType.LEFT_LEG_X, "R-Click armor stand to change left leg X value",
            "Value depends on how high up the body you click")
    val leftLegY = makeTool(ItemTypes.BONE, ToolType.LEFT_LEG_Y, "R-Click armor stand to change left leg Y value",
            "Value depends on how high up the body you click")
    val leftLegZ = makeTool(ItemTypes.BONE, ToolType.LEFT_LEG_Z, "R-Click armor stand to change left leg Z value",
            "Value depends on how high up the body you click")
    val rightArmX = makeTool(ItemTypes.REDSTONE_TORCH, ToolType.RIGHT_ARM_X, "R-Click armor stand to change right arm X value",
            "Value depends on how high up the body you click")
    val rightArmY = makeTool(ItemTypes.REDSTONE_TORCH, ToolType.RIGHT_ARM_Y, "R-Click armor stand to change right arm Y value",
            "Value depends on how high up the body you click")
    val rightArmZ = makeTool(ItemTypes.REDSTONE_TORCH, ToolType.RIGHT_ARM_Z, "R-Click armor stand to change right arm Z value",
            "Value depends on how high up the body you click")
    val rightLegX = makeTool(ItemTypes.BLAZE_ROD, ToolType.RIGHT_LEG_X, "R-Click armor stand to change right leg X value",
            "Value depends on how high up the body you click")
    val rightLegY = makeTool(ItemTypes.BLAZE_ROD, ToolType.RIGHT_LEG_Y, "R-Click armor stand to change right leg Y value",
            "Value depends on how high up the body you click")
    val rightLegZ = makeTool(ItemTypes.BLAZE_ROD, ToolType.RIGHT_LEG_Z, "R-Click armor stand to change right leg Z value",
            "Value depends on how high up the body you click")
    val bodyX = makeTool(ItemTypes.NETHERBRICK, ToolType.BODY_X, "R-Click armor stand to change body X value",
            "Value depends on how high up the body you click")
    val bodyY = makeTool(ItemTypes.NETHERBRICK, ToolType.BODY_Y, "R-Click armor stand to change body Y value",
            "Value depends on how high up the body you click")
    val bodyZ = makeTool(ItemTypes.NETHERBRICK, ToolType.BODY_Z, "R-Click armor stand to change body Z value",
            "Value depends on how high up the body you click")
    val headX = makeTool(ItemTypes.LIT_PUMPKIN, ToolType.HEAD_X, "R-Click armor stand to change head X value",
            "Value depends on how high up the body you click")
    val headY = makeTool(ItemTypes.LIT_PUMPKIN, ToolType.HEAD_Y, "R-Click armor stand to change head Y value",
            "Value depends on how high up the body you click")
    val headZ = makeTool(ItemTypes.LIT_PUMPKIN, ToolType.HEAD_Z, "R-Click armor stand to change head Z value",
            "Value depends on how high up the body you click")

    private fun makeTool(type: ItemType, toolType: ToolType, name: String, vararg lore: String): ItemStack {
        return itemStackOf {
            itemType(type)
            add(Keys.DISPLAY_NAME, name.yellow())
            add(Keys.ITEM_LORE, lore.map(String::green) as List<Text>)
        }.apply { getOrCreate(ToolData::class.java).get().also { this += it } }.also { it[ToolKeys.TOOL_TYPE] = toolType }
    }

    fun all(): List<ItemStack> {
        return listOf(summon, gui, rotation, moveX, moveY, moveZ,
                leftArmX, leftArmY, leftArmZ, rightArmX, rightArmY, rightArmZ,
                leftLegX, leftLegY, leftLegZ, rightLegX, rightLegY, rightLegZ,
                bodyX, bodyY, bodyZ, headX, headY, headZ)
    }
}