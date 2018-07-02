package flavor.pie.armorstandtools;

import com.google.common.reflect.TypeToken;
import java.util.List;
import javax.annotation.Generated;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.ListValue;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-07-01T23:45:42.489Z")
public class StoredCommandKeys {

    private StoredCommandKeys() {}

    public final static Key<ListValue<String>> COMMANDS;
    static {
        TypeToken<List<String>> listStringToken = new TypeToken<List<String>>(){};
        TypeToken<ListValue<String>> listValueStringToken = new TypeToken<ListValue<String>>(){};
//        COMMANDS = KeyFactory.makeListKey(listStringToken, listValueStringToken, DataQuery.of("Commands"), "armorstandtools:commands", "Commands");
        COMMANDS = Key.builder()
                .type(listValueStringToken)
                .id("commands")
                .name("Commands")
                .query(DataQuery.of("Commands"))
                .build();
    }
}
