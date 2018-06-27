package flavor.pie.armorstandtools;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Map;

import javax.annotation.Generated;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T10:28:43.461Z")
public class SavedInventoryKeys {

    private SavedInventoryKeys() {}

    public final static Key<MapValue<Integer, ItemStackSnapshot>> INVENTORY;
    public final static Key<MapValue<Integer, ItemStackSnapshot>> HOTBAR;
    public final static Key<Value<Boolean>> HAS_TOOLS;
    static {
        TypeToken<Map<Integer, ItemStackSnapshot>> mapIntegerItemStackSnapshotToken = new TypeToken<Map<Integer, ItemStackSnapshot>>(){};
        TypeToken<MapValue<Integer, ItemStackSnapshot>> mapValueIntegerItemStackSnapshotToken = new TypeToken<MapValue<Integer, ItemStackSnapshot>>(){};
        TypeToken<Boolean> booleanToken = TypeToken.of(Boolean.class);
        TypeToken<Value<Boolean>> valueBooleanToken = new TypeToken<Value<Boolean>>(){};
//        INVENTORY = KeyFactory.makeMapKey(mapIntegerItemStackSnapshotToken, mapValueIntegerItemStackSnapshotToken, DataQuery.of("Inventory"), "armorstandtools:inventory", "Inventory");
//        HOTBAR = KeyFactory.makeMapKey(mapIntegerItemStackSnapshotToken, mapValueIntegerItemStackSnapshotToken, DataQuery.of("Hotbar"), "armorstandtools:hotbar", "Hotbar");
//        HAS_TOOLS = KeyFactory.makeSingleKey(booleanToken, valueBooleanToken, DataQuery.of("HasTools"), "armorstandtools:hastools", "Has Tools");
        INVENTORY = Key.builder()
                .type(mapValueIntegerItemStackSnapshotToken)
                .id("inventory")
                .name("Inventory")
                .query(DataQuery.of("Inventory"))
                .build();
        HOTBAR = Key.builder()
                .type(mapValueIntegerItemStackSnapshotToken)
                .id("hotbar")
                .name("Hotbar")
                .query(DataQuery.of("Hotbar"))
                .build();
        HAS_TOOLS = Key.builder()
                .type(valueBooleanToken)
                .id("hasTools")
                .name("Has Tools")
                .query(DataQuery.of("HasTools"))
                .build();
    }
}
