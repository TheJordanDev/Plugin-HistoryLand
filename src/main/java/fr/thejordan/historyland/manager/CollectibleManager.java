package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.BItem;
import fr.thejordan.historyland.object.Keys;
import fr.thejordan.historyland.object.collectible.Collectible;
import fr.thejordan.historyland.object.collectible.CollectibleConfig;
import fr.thejordan.historyland.object.collectible.PlayersItems;
import fr.thejordan.historyland.object.shop.BoughtItemsConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class CollectibleManager extends AbstractManager {

    private static CollectibleManager instance;
    public static CollectibleManager instance() { return instance; }

    @Getter private final CollectibleConfig collectibleConfig;
    @Getter private final BoughtItemsConfig boughtItemsConfig;

    @Getter private Map<String, Collectible> collectible;
    @Getter private Map<UUID, PlayersItems> owned;

    public CollectibleManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.collectibleConfig = new CollectibleConfig(plugin);
        this.boughtItemsConfig = new BoughtItemsConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.collectible = collectibleConfig.load().stream().collect(Collectors.toMap(Collectible::getId, c -> c));
        if (collectible.size() == 0) createPlaceholder();
        this.owned = boughtItemsConfig.load().stream().collect(Collectors.toMap(PlayersItems::getUuid, p -> p));
    }

    private void createPlaceholder() {
        InputStreamReader defaultCollectibleFile = FileHelper.getStreamFromResource("defaultCollectibles.yml");
        File defaultCollectibleInFile = new File(plugin().getDataFolder(),"/collectibles.yml");
        if (defaultCollectibleFile != null) {
            try { YamlConfiguration.loadConfiguration(defaultCollectibleFile).save(defaultCollectibleInFile); }
            catch (Exception ignored) { }
        }
    }

    @Override
    public void onReload() {
        super.onReload();
        collectible.clear();
        this.collectible = collectibleConfig.load().stream().collect(Collectors.toMap(Collectible::getId, c -> c));
        if (collectible.size() == 0) createPlaceholder();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        boughtItemsConfig.save(new ArrayList<>(owned.values()));
    }

    @Override
    public List<Listener> listeners() {
        return null;
    }

    @Override
    public List<AbstractCommand> commands() {
        return null;
    }

    public static boolean isCollectible(ItemStack stack) {
        if (stack == null || stack.getItemMeta() == null || stack.getType().equals(Material.AIR)) return false;
        BItem item = BItem.of(stack);
        return item.hData(Keys.COLLECTIBLE_KEY, PersistentDataType.STRING);
    }

    public static String getIDFromCollectible(ItemStack stack) {
        if (!isCollectible(stack)) return "";
        BItem item = BItem.of(stack);
        return item.gData(Keys.COLLECTIBLE_KEY, PersistentDataType.STRING);
    }

    public static Collectible fromItemStack(ItemStack itemStack) {
        if (isCollectible(itemStack)) {
            String id = getIDFromCollectible(itemStack);
            return getByID(id);
        }
        return null;
    }

    public static boolean isCosmeticSlot(int slot, InventoryType.SlotType type) {
        return type.equals(InventoryType.SlotType.ARMOR) || slot == 45;
    }


    public static Collectible getByID(String id) {
        return instance().collectible.getOrDefault(id, Collectible.unknownItem);
    }

    public static void add(UUID uniqueId, Collectible collectible) {
        if (!instance.owned.containsKey(uniqueId))
            instance.owned.put(uniqueId,new PlayersItems(uniqueId,new ArrayList<>()));
        instance.owned.get(uniqueId).items().add(collectible.getId());
    }

    public static <T extends Collectible> boolean ownes(Player player, T item) {
        if (!instance.getOwned().containsKey(player.getUniqueId())) return false;
        PlayersItems owned = instance.getOwned().get(player.getUniqueId());
        return owned.items().contains(item.getId());
    }

    public static ArrayList<Collectible> getPlayers(Player player) {
        if (instance.owned.containsKey(player.getUniqueId()))
            return instance.owned.get(player.getUniqueId()).items().stream().map(CollectibleManager::getByID).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
        return new ArrayList<>();
    }


}
