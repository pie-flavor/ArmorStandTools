package flavor.pie.armorstandtools

import com.google.common.reflect.TypeToken
import flavor.pie.kludge.*
import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.objectmapping.ObjectMappingException
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer
import org.spongepowered.api.data.DataContainer
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.DataTranslators
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.text.Text

@ConfigSerializable
class Config {
    companion object {
        val type: TypeToken<Config> = TypeToken.of(Config::class.java)
    }
    @Setting("default-gear") var defaultGear: DefaultGearSection = DefaultGearSection()
    @Setting("default-settings") var defaultSettings: DefaultSettingsSection = DefaultSettingsSection()
    @Setting var version: Int = 1
}

@ConfigSerializable
class DefaultGearSection {
    @Setting var helmet: ItemStackSnapshot? = null
    @Setting var chestplate: ItemStackSnapshot? = null
    @Setting var leggings: ItemStackSnapshot? = null
    @Setting var boots: ItemStackSnapshot? = null
    @Setting("main-hand") var mainHand: ItemStackSnapshot? = null
    @Setting("off-hand") var offHand: ItemStackSnapshot? = null
}

@ConfigSerializable
class DefaultSettingsSection {
    @Setting var visible: Boolean = false
    @Setting var arms: Boolean = false
    @Setting("base-plate") var basePlate: Boolean = false
    @Setting var gravity: Boolean = false
    @Setting("name") private var nameString: String? = null
    val name: Text? by lazy { nameString?.textByFormat() }
    @Setting var invulnerable: Boolean = false
    @Setting var small: Boolean = false
    @Setting("equipment-lock") var equipmentLock: EquipmentLockSection = EquipmentLockSection()
}

@ConfigSerializable
class EquipmentLockSection {
    @Setting var hands: LockType = LockType.NONE
    @Setting var helmet: LockType = LockType.NONE
    @Setting var chestplate: LockType = LockType.NONE
    @Setting var leggings: LockType = LockType.NONE
    @Setting var boots: LockType = LockType.NONE
}

enum class LockType {
    NONE, BOTH, REMOVE, PLACE
}

object ItemStackSnapshotSerializer : TypeSerializer<ItemStackSnapshot> {
    override fun deserialize(type: TypeToken<*>, value: ConfigurationNode): ItemStackSnapshot? {
        if (value.isVirtual) return null
        if (!value.hasMapChildren()) {
            val itemType = value.getValue(TypeToken.of(ItemType::class.java))
            return ItemStack.of(itemType, 1).createSnapshot()
        }
        val data = DataTranslators.CONFIGURATION_NODE.translate(value)
        val damage = data.getInt(DataQuery.of("Damage")).orElse(0)
        val item = data.getCatalogType(DataQuery.of("id"), ItemType::class.java).unwrap()
                ?: throw ObjectMappingException("id: invalid item type '${value.getNode("id").value}'")
        val count = data.getInt(DataQuery.of("Count")).unwrap() ?: 1
        val container = DataContainer.createNew()
        data.remove(DataQuery.of("Count"))
        data.remove(DataQuery.of("Damage"))
        data.remove(DataQuery.of("id"))
        container.set(DataQuery.of("UnsafeDamage"), damage)
        container.set(DataQuery.of("UnsafeData"), data)
        container.set(DataQuery.of("ItemType"), item)
        container.set(DataQuery.of("Count"), count)
        return itemStackOf { fromContainer(container) }.createSnapshot()
    }

    override fun serialize(type: TypeToken<*>, obj: ItemStackSnapshot?, value: ConfigurationNode) {
        if (obj == null) {
            value.value = null
            return
        }
        val data = obj.toContainer()
        val view = data.getView(DataQuery.of("UnsafeData")).get()
        val damage = data.getInt(DataQuery.of("UnsafeDamage")).orElse(0)
        val count = data.getInt(DataQuery.of("Count")).orElse(1)
        val id = data.getObject(DataQuery.of("ItemType"), ItemType::class.java).get()
        view.set(DataQuery.of("Damage"), damage)
        view.set(DataQuery.of("id"), id)
        view.set(DataQuery.of("Count"), count)
        value.setValue(TypeToken.of(DataView::class.java), view)
    }

}
