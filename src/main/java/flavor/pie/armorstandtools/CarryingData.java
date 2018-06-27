package flavor.pie.armorstandtools;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;
import javax.annotation.Nullable;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableOptionalValue;
import org.spongepowered.api.data.value.mutable.OptionalValue;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T05:38:15.114Z")
public class CarryingData extends AbstractData<CarryingData, CarryingData.Immutable> {

    private UUID carrying;
    private Transform<World> originalLocation;

    {
        registerGettersAndSetters();
    }

    CarryingData() {
    }

    CarryingData(UUID carrying, Transform<World> originalLocation) {
        this.carrying = carrying;
        this.originalLocation = originalLocation;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(CarryingKeys.CARRYING, this::getCarrying);
        registerFieldSetter(CarryingKeys.CARRYING, v -> setCarrying(v.orElse(null)));
        registerKeyValue(CarryingKeys.CARRYING, this::carrying);
        registerFieldGetter(CarryingKeys.ORIGINAL_LOCATION, this::getOriginalLocation);
        registerFieldSetter(CarryingKeys.ORIGINAL_LOCATION, v -> setOriginalLocation(v.orElse(null)));
        registerKeyValue(CarryingKeys.ORIGINAL_LOCATION, this::originalLocation);
    }

    public Optional<UUID> getCarrying() {
        return Optional.ofNullable(carrying);
    }

    public void setCarrying(@Nullable UUID carrying) {
        this.carrying = carrying;
    }

    public OptionalValue<UUID> carrying() {
        return Sponge.getRegistry().getValueFactory().createOptionalValue(CarryingKeys.CARRYING, carrying);
    }

    public Optional<Transform<World>> getOriginalLocation() {
        return Optional.ofNullable(originalLocation);
    }

    public void setOriginalLocation(@Nullable Transform<World> originalLocation) {
        this.originalLocation = originalLocation;
    }

    public OptionalValue<Transform<World>> originalLocation() {
        return Sponge.getRegistry().getValueFactory().createOptionalValue(CarryingKeys.ORIGINAL_LOCATION, originalLocation);
    }

    @Override
    public Optional<CarryingData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(CarryingData.class).ifPresent(that -> {
                CarryingData data = overlap.merge(this, that);
                this.carrying = data.carrying;
                this.originalLocation = data.originalLocation;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<CarryingData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<CarryingData> from(DataView container) {
        container.getObject(CarryingKeys.CARRYING.getQuery(), UUID.class).ifPresent(v -> carrying = v);
        container.getView(CarryingKeys.ORIGINAL_LOCATION.getQuery()).ifPresent(v -> {
            Vector3d position = v.getObject(DataQuery.of("position"), Vector3d.class).get();
            Vector3d rotation = v.getObject(DataQuery.of("rotation"), Vector3d.class).get();
            Sponge.getServer().getWorld(v.getObject(DataQuery.of("world"), UUID.class).get()).ifPresent(w -> {
                originalLocation = new Transform<>(w, position, rotation);
            });
        });
        return Optional.of(this);
    }

    @Override
    public CarryingData copy() {
        return new CarryingData(carrying, originalLocation);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(carrying, originalLocation);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        if (carrying != null) {
            container.set(CarryingKeys.CARRYING.getQuery(), carrying);
        }
        if (originalLocation != null) {
            container.set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("position"), originalLocation.getPosition())
                    .set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("rotation"), originalLocation.getRotation())
                    .set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("world"), originalLocation.getExtent().getUniqueId());
        }
        return container;
    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T05:38:15.131Z")
    public static class Immutable extends AbstractImmutableData<Immutable, CarryingData> {

        private UUID carrying;
        private Transform<World> originalLocation;
        {
            registerGetters();
        }

        Immutable() {
        }

        Immutable(UUID carrying, Transform<World> originalLocation) {
            this.carrying = carrying;
            this.originalLocation = originalLocation;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(CarryingKeys.CARRYING, this::getCarrying);
            registerKeyValue(CarryingKeys.CARRYING, this::carrying);
            registerFieldGetter(CarryingKeys.ORIGINAL_LOCATION, this::getOriginalLocation);
            registerKeyValue(CarryingKeys.ORIGINAL_LOCATION, this::originalLocation);
        }

        public Optional<UUID> getCarrying() {
            return Optional.ofNullable(carrying);
        }

        public ImmutableOptionalValue<UUID> carrying() {
            return (ImmutableOptionalValue<UUID>) Sponge.getRegistry().getValueFactory().createOptionalValue(CarryingKeys.CARRYING, carrying).asImmutable();
        }

        public Optional<Transform<World>> getOriginalLocation() {
            return Optional.ofNullable(originalLocation);
        }

        public ImmutableOptionalValue<Transform<World>> originalLocation() {
            return (ImmutableOptionalValue<Transform<World>>) Sponge.getRegistry().getValueFactory().createOptionalValue(CarryingKeys.ORIGINAL_LOCATION, originalLocation).asImmutable();
        }

        @Override
        public CarryingData asMutable() {
            return new CarryingData(carrying, originalLocation);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer()
                    .set(CarryingKeys.CARRYING.getQuery(), carrying)
                    .set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("position"), originalLocation.getPosition())
                    .set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("rotation"), originalLocation.getRotation())
                    .set(CarryingKeys.ORIGINAL_LOCATION.getQuery().then("world"), originalLocation.getExtent().getUniqueId());
        }

    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T05:38:15.134Z")
    public static class Builder extends AbstractDataBuilder<CarryingData> implements DataManipulatorBuilder<CarryingData, Immutable> {

        protected Builder() {
            super(CarryingData.class, 1);
        }

        @Override
        public CarryingData create() {
            return new CarryingData();
        }

        @Override
        public Optional<CarryingData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<CarryingData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }

}
