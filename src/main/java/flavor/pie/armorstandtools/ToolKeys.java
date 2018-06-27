package flavor.pie.armorstandtools;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import javax.annotation.Generated;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T01:09:58.704Z")
public class ToolKeys {

    private ToolKeys() {}

    public final static Key<Value<ToolType>> TOOL_TYPE;
    static {
        TypeToken<ToolType> toolTypeToken = TypeToken.of(ToolType.class);
        TypeToken<Value<ToolType>> valueToolTypeToken = new TypeToken<Value<ToolType>>(){};
//        TOOL_TYPE = KeyFactory.makeSingleKey(toolTypeToken, valueToolTypeToken, DataQuery.of("ToolType"), "armorstandtools:tooltype", "Tool Type");
        TOOL_TYPE = Key.builder()
                .type(valueToolTypeToken)
                .id("tool_type")
                .name("Tool Type")
                .query(DataQuery.of("ToolType"))
                .build();
    }
}
