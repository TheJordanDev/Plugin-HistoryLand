package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.ShopCommand;
import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.shop.Shop;
import fr.thejordan.historyland.object.shop.ShopConfig;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
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

}
