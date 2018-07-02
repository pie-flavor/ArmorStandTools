package flavor.pie.armorstandtools

import com.google.common.reflect.TypeToken
import com.google.inject.Inject
import flavor.pie.kludge.*
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.bstats.sponge.MetricsLite
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.data.Has
import org.spongepowered.api.event.game.GameRegistryEvent
import org.spongepowered.api.event.game.GameReloadEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@Plugin(id = "armorstandtools", name = "ArmorStandTools", version = "1.0.1-SNAPSHOT", authors = ["pie_flavor"],
        dependencies = [Dependency(id = "huskyui", version = "0.5.1", optional = true)], description = "Tools for manipulating armor stands.")
class ArmorStandTools @Inject constructor(private val plugin: PluginContainer,
                                          @DefaultConfig(sharedRoot = true) private val path: Path,
                                          @DefaultConfig(sharedRoot = true) private val loader: ConfigurationLoader<CommentedConfigurationNode>,
                                          private val metrics: MetricsLite) {

    companion object {
        lateinit var instance: ArmorStandTools
        val config get() = instance.config
    }

    private val playerQueue: MutableMap<UUID, (String) -> Unit> = mutableMapOf()

    private lateinit var config: Config

    init {
        instance = this
    }

    @Listener
    fun onJoinWithoutSavedInventoryData(e: ClientConnectionEvent.Join,
            @Getter("getTargetEntity") @Has(SavedInventoryData::class, inverse = true) player: Player) {
        player += player.getOrCreate(SavedInventoryData::class.java).get()
    }

    @Listener
    fun onJoinWithoutCarryingData(e: ClientConnectionEvent.Join,
            @Getter("getTargetEntity") @Has(CarryingData::class, inverse = true) player: Player) {
        player += player.getOrCreate(CarryingData::class.java).get()
    }

    @Listener
    fun registerKeys(e: GameRegistryEvent.Register<Key<*>>) {
        e.register(CarryingKeys.CARRYING)
        e.register(CarryingKeys.ORIGINAL_LOCATION)
        e.register(SavedInventoryKeys.HAS_TOOLS)
        e.register(SavedInventoryKeys.HOTBAR)
        e.register(SavedInventoryKeys.INVENTORY)
        e.register(ToolKeys.TOOL_TYPE)
        e.register(StoredCommandKeys.COMMANDS)
    }

    @Listener
    fun preInit(e: GamePreInitializationEvent) {
        dataRegistrationOf<CarryingData, CarryingData.Immutable>(plugin) {
            dataName("Carrying Data")
            manipulatorId("carrying_data")
            builder(CarryingData.Builder())
        }
        dataRegistrationOf<SavedInventoryData, SavedInventoryData.Immutable>(plugin) {
            dataName("Saved Inventory Data")
            manipulatorId("saved_inventory_data")
            builder(SavedInventoryData.Builder())
        }
        dataRegistrationOf<ToolData, ToolData.Immutable>(plugin) {
            dataName("Tool Data")
            manipulatorId("tool_data")
            builder(ToolData.Builder())
        }
        dataRegistrationOf<StoredCommandData, StoredCommandData.Immutable>(plugin) {
            dataName("Stored Command Data")
            manipulatorId("stored_command_data")
            builder(StoredCommandData.Builder())
        }
        Commands.register(this)
        EventManager.registerListeners(this, ToolListeners)
        EventManager.registerListeners(this, GuiListener)
        EventManager.registerListeners(this, InteractionListener)
        loadConfig()
    }

    fun loadConfig() {
        if (!Files.exists(path)) {
            AssetManager.getAsset(this, "default.conf").get().copyToFile(path)
        }
        val opts = loader.defaultOptions.let { it.setSerializers(it.serializers.newChild().registerType(TypeToken.of(ItemStackSnapshot::class.java), ItemStackSnapshotSerializer)) }
        config = loader.load(opts).getValue(Config.type)
    }

    fun whenPlayerNextSpeaks(p: Player, fn: (String) -> Unit) {
        playerQueue[p.uniqueId] = fn
    }

    @Listener
    fun onChat(e: MessageChannelEvent.Chat, @First p: Player) {
        if (p.uniqueId in playerQueue) {
            e.isCancelled = true
            playerQueue.remove(p.uniqueId)!!(e.rawMessage.toPlain())
        }
     }

    @Listener
    fun reload(e: GameReloadEvent) {
        loadConfig()
    }

}
