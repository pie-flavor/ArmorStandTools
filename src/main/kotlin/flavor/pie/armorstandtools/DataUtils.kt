package flavor.pie.armorstandtools

import flavor.pie.kludge.*
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack

fun Entity.getVanillaData(): DataContainer {
    val container = toContainer()
    val ret = DataContainer.createNew()
    val view = container.getView(DataQuery.of("UnsafeData")).get()
    for (key in view.getKeys(false)) {
        ret[key] = view[key].get()
    }
    ret.remove(DataQuery.of("Pos"))
    return ret
}

fun Entity.toRawContainer(): DataContainer {
    val container = toContainer()
    val view = container.getView(DataQuery.of("UnsafeData")).get()
    val ret = DataContainer.createNew()
    for (key in view.getKeys(false)) {
        ret[key] = view[key].get()
    }
    ret.remove(DataQuery.of("UUIDLeast"))
    ret.remove(DataQuery.of("UUIDMost"))
    return ret
}

// SC#1962
fun createCommandBlock(command: String): ItemStack {
    val stack = ItemStack.of(ItemTypes.COMMAND_BLOCK, 1)
    val container = stack.toContainer()
    container[DataQuery.of("UnsafeData", "BlockEntityTag", "Command")] = command
    return itemStackOf {
        fromContainer(container)
    }
}
