package flavor.pie.armorstandtools;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
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
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.mutable.ListValue;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-07-01T23:45:42.466Z")
public class StoredCommandData extends AbstractData<StoredCommandData, StoredCommandData.Immutable> {

    private List<String> commands;

    {
        registerGettersAndSetters();
    }

    StoredCommandData() {
        commands = Collections.emptyList();
    }

    StoredCommandData(List<String> commands) {
        this.commands = commands;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(StoredCommandKeys.COMMANDS, this::getCommands);
        registerFieldSetter(StoredCommandKeys.COMMANDS, this::setCommands);
        registerKeyValue(StoredCommandKeys.COMMANDS, this::commands);
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public ListValue<String> commands() {
        return Sponge.getRegistry().getValueFactory().createListValue(StoredCommandKeys.COMMANDS, commands);
    }

    @Override
    public Optional<StoredCommandData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(StoredCommandData.class).ifPresent(that -> {
                StoredCommandData data = overlap.merge(this, that);
                this.commands = data.commands;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<StoredCommandData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<StoredCommandData> from(DataView container) {
        container.getStringList(StoredCommandKeys.COMMANDS.getQuery()).ifPresent(v -> commands = v);
        return Optional.of(this);
    }

    @Override
    public StoredCommandData copy() {
        return new StoredCommandData(commands);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(commands);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(StoredCommandKeys.COMMANDS.getQuery(), commands);
    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-07-01T23:45:42.484Z")
    public static class Immutable extends AbstractImmutableData<Immutable, StoredCommandData> {

        private List<String> commands;
        {
            registerGetters();
        }

        Immutable() {
            commands = Collections.emptyList();
        }

        Immutable(List<String> commands) {
            this.commands = commands;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(StoredCommandKeys.COMMANDS, this::getCommands);
            registerKeyValue(StoredCommandKeys.COMMANDS, this::commands);
        }

        public List<String> getCommands() {
            return commands;
        }

        public ImmutableListValue<String> commands() {
            return Sponge.getRegistry().getValueFactory().createListValue(StoredCommandKeys.COMMANDS, commands).asImmutable();
        }

        @Override
        public StoredCommandData asMutable() {
            return new StoredCommandData(commands);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer()
                    .set(StoredCommandKeys.COMMANDS.getQuery(), commands);
        }

    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-07-01T23:45:42.488Z")
    public static class Builder extends AbstractDataBuilder<StoredCommandData> implements DataManipulatorBuilder<StoredCommandData, Immutable> {

        protected Builder() {
            super(StoredCommandData.class, 1);
        }

        @Override
        public StoredCommandData create() {
            return new StoredCommandData();
        }

        @Override
        public Optional<StoredCommandData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<StoredCommandData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }
}
