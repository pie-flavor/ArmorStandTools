package flavor.pie.armorstandtools;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

import javax.annotation.Generated;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T01:09:58.684Z")
public class ToolData extends AbstractData<ToolData, ToolData.Immutable> {

    private ToolType toolType;

    {
        registerGettersAndSetters();
    }

    ToolData() {
        toolType = ToolType.NONE;
    }

    ToolData(ToolType toolType) {
        this.toolType = toolType;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(ToolKeys.TOOL_TYPE, this::getToolType);
        registerFieldSetter(ToolKeys.TOOL_TYPE, this::setToolType);
        registerKeyValue(ToolKeys.TOOL_TYPE, this::toolType);
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public Value<ToolType> toolType() {
        return Sponge.getRegistry().getValueFactory().createValue(ToolKeys.TOOL_TYPE, toolType);
    }

    @Override
    public Optional<ToolData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(ToolData.class).ifPresent(that -> {
                ToolData data = overlap.merge(this, that);
                this.toolType = data.toolType;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<ToolData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<ToolData> from(DataView container) {
        container.getString(ToolKeys.TOOL_TYPE.getQuery()).ifPresent(v -> toolType = ToolType.valueOf(v));
        return Optional.of(this);
    }

    @Override
    public ToolData copy() {
        return new ToolData(toolType);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(toolType);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(ToolKeys.TOOL_TYPE.getQuery(), toolType.toString());
    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T01:09:58.701Z")
    public static class Immutable extends AbstractImmutableData<Immutable, ToolData> {

        private ToolType toolType;
        {
            registerGetters();
        }

        Immutable() {
            toolType = ToolType.NONE;
        }

        Immutable(ToolType toolType) {
            this.toolType = toolType;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(ToolKeys.TOOL_TYPE, this::getToolType);
            registerKeyValue(ToolKeys.TOOL_TYPE, this::toolType);
        }

        public ToolType getToolType() {
            return toolType;
        }

        public ImmutableValue<ToolType> toolType() {
            return Sponge.getRegistry().getValueFactory().createValue(ToolKeys.TOOL_TYPE, toolType).asImmutable();
        }

        @Override
        public ToolData asMutable() {
            return new ToolData(toolType);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer()
                    .set(ToolKeys.TOOL_TYPE.getQuery(), toolType.toString());
        }

    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T01:09:58.703Z")
    public static class Builder extends AbstractDataBuilder<ToolData> implements DataManipulatorBuilder<ToolData, Immutable> {

        protected Builder() {
            super(ToolData.class, 1);
        }

        @Override
        public ToolData create() {
            return new ToolData();
        }

        @Override
        public Optional<ToolData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ToolData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }
}
