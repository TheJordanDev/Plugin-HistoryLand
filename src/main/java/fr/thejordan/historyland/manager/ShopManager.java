package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.ShopCommand;
import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.shop.Shop;
import fr.thejordan.historyland.object.shop.ShopConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ShopManager extends AbstractManager {

    private static ShopManager instance;
    public static ShopManager instance() { return instance;}

    @Getter private Map<String, Shop> shops;
    @Getter private final ShopConfig shopConfig;

    public ShopManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.shopConfig = new ShopConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.shops = shopConfig.load().stream().collect(java.util.stream.Collectors.toMap(Shop::getName, shop -> shop));
        if (shops.size() == 0)  { createPlaceholder(); onReload(); }
    }

    @Override
    public void onDisable() {
        super.onDisable();

    }

    @Override
    public void onReload() {
        super.onReload();
        this.shops = shopConfig.load().stream().collect(java.util.stream.Collectors.toMap(Shop::getName, shop -> shop));
        if (shops.size() == 0) { createPlaceholder(); onReload(); }
    }

    private void createPlaceholder() {
        InputStreamReader defaultShopFile = FileHelper.getStreamFromResource("defaultShop.yml");
        File defaultShopInFile = new File(plugin().getDataFolder(),"/shops/default.yml");
        if (defaultShopFile != null && !defaultShopInFile.exists()) {
            try { YamlConfiguration.loadConfiguration(defaultShopFile).save(defaultShopInFile); }
            catch (Exception ignored) {}
        }
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new ShopCommand());
    }

    @EventHandler
    public void onInteractSeller(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof ArmorStand stand)) return;
        ItemStack offHand = player.getInventory().getItemInOffHand();
        BItem item = BItem.of(offHand);
        PersistentDataContainer data = stand.getPersistentDataContainer();
        boolean isStaff = isItemShopStick(offHand);
        if (stand.getPersistentDataContainer().has(Keys.SHOP, PersistentDataType.STRING)) {
            event.setCancelled(true);
            if (player.isSneaking() && isStaff) {
                data.remove(Keys.SHOP);
                player.sendMessage("§c§lSHOP supprimé");
            } else {
                String shopName = stand.getPersistentDataContainer().get(Keys.SHOP, PersistentDataType.STRING);
                if (!getShops().containsKey(shopName)) return;
                Shop shop = getShops().get(shopName);
                shop.open(player);
            }
        } else {
            if (item.getType() == Material.AIR) return;
            if (item.getType() != Material.BLAZE_ROD) return;
            if (!item.hData(Keys.SHOP_OWNER_KEY,PersistentDataType.STRING)) return;
            event.setCancelled(true);
            Shop shop = getShops().get(item.gData(Keys.SHOP_OWNER_KEY, PersistentDataType.STRING));
            if (shop == null) { player.sendMessage("§cShop inconnu."); return; }
            data.set(Keys.SHOP, PersistentDataType.STRING, shop.getName());
            player.sendMessage("§a§lSHOP définis sur §6" + shop.getName());
        }
    }

    private boolean isItemShopStick(ItemStack stack) {
        BItem item = BItem.of(stack);
        if (item.getType() == Material.AIR) return false;
        if (item.getType() != Material.BLAZE_ROD) return false;
        return item.hData(Keys.SHOP_OWNER_KEY, PersistentDataType.STRING);
    }

}
