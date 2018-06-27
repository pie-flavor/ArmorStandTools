package flavor.pie.armorstandtools;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Generated;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T05:38:15.136Z")
public class CarryingKeys {

    private CarryingKeys() {}

    public final static Key<OptionalValue<UUID>> CARRYING;
    public final static Key<OptionalValue<Transform<World>>> ORIGINAL_LOCATION;
    static {
        TypeToken<Optional<UUID>> optionalUUIDToken = new TypeToken<Optional<UUID>>(){};
        TypeToken<OptionalValue<UUID>> optionalValueUUIDToken = new TypeToken<OptionalValue<UUID>>(){};
        TypeToken<Optional<Transform<World>>> optionalTransformWorldToken = new TypeToken<Optional<Transform<World>>>(){};
        TypeToken<OptionalValue<Transform<World>>> optionalValueTransformWorldToken = new TypeToken<OptionalValue<Transform<World>>>(){};
//        CARRYING = KeyFactory.makeOptionalKey(optionalUUIDToken, optionalValueUUIDToken, DataQuery.of("Carrying"), "armorstandtools:carrying", "Carrying");
//        ORIGINAL_LOCATION = KeyFactory.makeOptionalKey(optionalTransformWorldToken, optionalValueTransformWorldToken, DataQuery.of("OriginalLocation"), "armorstandtools:originallocation", "Original Location");
        CARRYING = Key.builder()
                .type(optionalValueUUIDToken)
                .id("carrying")
                .name("Carrying")
                .query(DataQuery.of("Carrying"))
                .build();
        ORIGINAL_LOCATION = Key.builder()
                .type(optionalValueTransformWorldToken)
                .id("original_location")
                .name("Original Location")
                .query(DataQuery.of("OriginalLocation"))
                .build();
    }
}
