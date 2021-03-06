package flavor.pie.armorstandtools

enum class ToolType {
    SUMMON, GUI, ROTATION, MOVE_X, MOVE_Y, MOVE_Z,
    LEFT_ARM_X, LEFT_ARM_Y, LEFT_ARM_Z, RIGHT_ARM_X, RIGHT_ARM_Y, RIGHT_ARM_Z,
    LEFT_LEG_X, LEFT_LEG_Y, LEFT_LEG_Z, RIGHT_LEG_X, RIGHT_LEG_Y, RIGHT_LEG_Z,
    BODY_X, BODY_Y, BODY_Z, HEAD_X, HEAD_Y, HEAD_Z,
    NONE;

    companion object {
        val REQUIRES_ANGLE: List<ToolType> = listOf(ROTATION, LEFT_ARM_X, LEFT_ARM_Y, LEFT_ARM_Z, RIGHT_ARM_X,
                RIGHT_ARM_Y, RIGHT_ARM_Z, LEFT_LEG_X, LEFT_LEG_Y, LEFT_LEG_Z, RIGHT_LEG_X, RIGHT_LEG_Y, RIGHT_LEG_Z,
                BODY_X, BODY_Y, BODY_Z, HEAD_X, HEAD_Y, HEAD_Z)
    }
}

fun ToolType?.isNone(): Boolean {
    return this == null || this == ToolType.NONE
}
