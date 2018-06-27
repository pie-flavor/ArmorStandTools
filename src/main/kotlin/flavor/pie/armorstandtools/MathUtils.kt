package flavor.pie.armorstandtools

import com.flowpowered.math.vector.Vector3d
import flavor.pie.kludge.*
import org.spongepowered.api.data.property.block.PassableProperty
import org.spongepowered.api.entity.Transform
import org.spongepowered.api.world.World

object MathUtils {
    fun getTransformFacing(transform: Transform<World>): Transform<World> {
        val direction = transform.direction.run { Vector3d(x, 0.0, z) } * 3.0
        val position = transform.position + direction
        val resulting = transform.setDirection(direction)
                .let { it.setRotation(it.rotation.add(0.0, 180.0, 0.0)) }
                .setPosition(position)
        if (resulting.location.blockType.getProperty(PassableProperty::class.java).unwrap()?.value != false) {
            return resulting
        }
        for (i in 0 until 5) {
            val test = resulting.setPosition(resulting.position.add(0.0, i.toDouble(), 0.0))
            if (test.location.blockType.getProperty(PassableProperty::class.java).unwrap()?.value != false) {
                return test
            }
        }
        return resulting
    }
}