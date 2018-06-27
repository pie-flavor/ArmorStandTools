package flavor.pie.armorstandtools;

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
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Generated;

@Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T10:28:43.421Z")
public class SavedInventoryData extends AbstractData<SavedInventoryData, SavedInventoryData.Immutable> {

    private Map<Integer, ItemStackSnapshot> inventory;
    private Map<Integer, ItemStackSnapshot> hotbar;
    private boolean hasTools;

    {
        registerGettersAndSetters();
    }

    SavedInventoryData() {
        inventory = Collections.emptyMap();
        hotbar = Collections.emptyMap();
        hasTools = false;
    }

    SavedInventoryData(Map<Integer, ItemStackSnapshot> inventory, Map<Integer, ItemStackSnapshot> hotbar, boolean hasTools) {
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.hasTools = hasTools;
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(SavedInventoryKeys.INVENTORY, this::getInventory);
        registerFieldSetter(SavedInventoryKeys.INVENTORY, this::setInventory);
        registerKeyValue(SavedInventoryKeys.INVENTORY, this::inventory);
        registerFieldGetter(SavedInventoryKeys.HOTBAR, this::getHotbar);
        registerFieldSetter(SavedInventoryKeys.HOTBAR, this::setHotbar);
        registerKeyValue(SavedInventoryKeys.HOTBAR, this::hotbar);
        registerFieldGetter(SavedInventoryKeys.HAS_TOOLS, this::isHasTools);
        registerFieldSetter(SavedInventoryKeys.HAS_TOOLS, this::setHasTools);
        registerKeyValue(SavedInventoryKeys.HAS_TOOLS, this::hasTools);
    }

    public Map<Integer, ItemStackSnapshot> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Integer, ItemStackSnapshot> inventory) {
        this.inventory = inventory;
    }

    public MapValue<Integer, ItemStackSnapshot> inventory() {
        return Sponge.getRegistry().getValueFactory().createMapValue(SavedInventoryKeys.INVENTORY, inventory);
    }

    public Map<Integer, ItemStackSnapshot> getHotbar() {
        return hotbar;
    }

    public void setHotbar(Map<Integer, ItemStackSnapshot> hotbar) {
        this.hotbar = hotbar;
    }

    public MapValue<Integer, ItemStackSnapshot> hotbar() {
        return Sponge.getRegistry().getValueFactory().createMapValue(SavedInventoryKeys.HOTBAR, hotbar);
    }

    public boolean isHasTools() {
        return hasTools;
    }

    public void setHasTools(boolean hasTools) {
        this.hasTools = hasTools;
    }

    public Value<Boolean> hasTools() {
        return Sponge.getRegistry().getValueFactory().createValue(SavedInventoryKeys.HAS_TOOLS, hasTools);
    }

    @Override
    public Optional<SavedInventoryData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(SavedInventoryData.class).ifPresent(that -> {
            SavedInventoryData data = overlap.merge(this, that);
            this.inventory = data.inventory;
            this.hotbar = data.hotbar;
            this.hasTools = data.hasTools;
        });
        return Optional.of(this);
    }

    @Override
    public Optional<SavedInventoryData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<SavedInventoryData> from(DataView container) {
        container.getView(SavedInventoryKeys.INVENTORY.getQuery()).ifPresent(v -> {
            Map<Integer, ItemStackSnapshot> map = new HashMap<>();
            for (DataQuery query : v.getKeys(false)) {
                map.put(Integer.parseInt(query.toString()),
                        ItemStack.builder().fromContainer(v.getView(query).get()).build().createSnapshot());
            }
            this.inventory = map;
        });
        container.getView(SavedInventoryKeys.HOTBAR.getQuery()).ifPresent(v -> {
            Map<Integer, ItemStackSnapshot> map = new HashMap<>();
            for (DataQuery query : v.getKeys(false)) {
                map.put(Integer.parseInt(query.toString()),
                        ItemStack.builder().fromContainer(v.getView(query).get()).build().createSnapshot());
            }
            this.hotbar = map;
        });
        container.getBoolean(SavedInventoryKeys.HAS_TOOLS.getQuery()).ifPresent(v -> hasTools = v);
        return Optional.of(this);
    }

    @Override
    public SavedInventoryData copy() {
        return new SavedInventoryData(inventory, hotbar, hasTools);
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(inventory, hotbar, hasTools);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = super.toContainer()
                .set(SavedInventoryKeys.HAS_TOOLS.getQuery(), hasTools);
        for (Map.Entry<Integer, ItemStackSnapshot> entry : inventory.entrySet()) {
            container.set(SavedInventoryKeys.INVENTORY.getQuery().then(entry.getKey().toString()), entry.getValue().toContainer());
        }
        for (Map.Entry<Integer, ItemStackSnapshot> entry : hotbar.entrySet()) {
            container.set(SavedInventoryKeys.HOTBAR.getQuery().then(entry.getKey().toString()), entry.getValue().toContainer());
        }
        return container;
    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T10:28:43.455Z")
    public static class Immutable extends AbstractImmutableData<Immutable, SavedInventoryData> {

        private Map<Integer, ItemStackSnapshot> inventory;
        private Map<Integer, ItemStackSnapshot> hotbar;
        private boolean hasTools;
        {
            registerGetters();
        }

        Immutable() {
            inventory = Collections.emptyMap();
            hotbar = Collections.emptyMap();
            hasTools = false;
        }

        Immutable(Map<Integer, ItemStackSnapshot> inventory, Map<Integer, ItemStackSnapshot> hotbar, boolean hasTools) {
            this.inventory = inventory;
            this.hotbar = hotbar;
            this.hasTools = hasTools;
        }

        @Override
        protected void registerGetters() {
            registerFieldGetter(SavedInventoryKeys.INVENTORY, this::getInventory);
            registerKeyValue(SavedInventoryKeys.INVENTORY, this::inventory);
            registerFieldGetter(SavedInventoryKeys.HOTBAR, this::getHotbar);
            registerKeyValue(SavedInventoryKeys.HOTBAR, this::hotbar);
            registerFieldGetter(SavedInventoryKeys.HAS_TOOLS, this::isHasTools);
            registerKeyValue(SavedInventoryKeys.HAS_TOOLS, this::hasTools);
        }

        public Map<Integer, ItemStackSnapshot> getInventory() {
            return inventory;
        }

        public ImmutableMapValue<Integer, ItemStackSnapshot> inventory() {
            return Sponge.getRegistry().getValueFactory().createMapValue(SavedInventoryKeys.INVENTORY, inventory).asImmutable();
        }

        public Map<Integer, ItemStackSnapshot> getHotbar() {
            return hotbar;
        }

        public ImmutableMapValue<Integer, ItemStackSnapshot> hotbar() {
            return Sponge.getRegistry().getValueFactory().createMapValue(SavedInventoryKeys.HOTBAR, hotbar).asImmutable();
        }

        public boolean isHasTools() {
            return hasTools;
        }

        public ImmutableValue<Boolean> hasTools() {
            return Sponge.getRegistry().getValueFactory().createValue(SavedInventoryKeys.HAS_TOOLS, hasTools).asImmutable();
        }

        @Override
        public SavedInventoryData asMutable() {
            return new SavedInventoryData(inventory, hotbar, hasTools);
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            DataContainer container = super.toContainer()
                    .set(SavedInventoryKeys.HAS_TOOLS.getQuery(), hasTools);
            for (Map.Entry<Integer, ItemStackSnapshot> entry : inventory.entrySet()) {
                container.set(SavedInventoryKeys.INVENTORY.getQuery().then(entry.getKey().toString()), entry.getValue().toContainer());
            }
            for (Map.Entry<Integer, ItemStackSnapshot> entry : hotbar.entrySet()) {
                container.set(SavedInventoryKeys.HOTBAR.getQuery().then(entry.getKey().toString()), entry.getValue().toContainer());
            }
            return container;
        }

    }

    @Generated(value = "flavor.pie.generator.data.DataManipulatorGenerator", date = "2018-05-21T10:28:43.460Z")
    public static class Builder extends AbstractDataBuilder<SavedInventoryData> implements DataManipulatorBuilder<SavedInventoryData, Immutable> {

        protected Builder() {
            super(SavedInventoryData.class, 1);
        }

        @Override
        public SavedInventoryData create() {
            return new SavedInventoryData();
        }

        @Override
        public Optional<SavedInventoryData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<SavedInventoryData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }

    }
}
