package fr.thejordan.historyland.object.shop;

import fr.thejordan.historyland.object.AbstractConfigFolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShopConfig extends AbstractConfigFolder<Shop> {

    public ShopConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File folder() {
        return new File(getPlugin().getDataFolder(), "shops");
    }

    @Override
    public boolean filter(File dir, String name) {
        return name.endsWith(".yml");
    }

    @Override
    public List<String> blacklist() {
        return List.of();
    }

    @Override
    public String name() {
        return "Shops";
    }

    @Override
    public Function<YamlConfiguration, Shop> loadProcess() {
        return (configuration)->{
            String name = configuration.getString("name", "Shop");
            ArrayList<ShopItem> items = new ArrayList<>();
            if (!configuration.isConfigurationSection("items")) return new Shop(name, items);
            ConfigurationSection itemsSection = configuration.getConfigurationSection("items");
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                items.add(ShopItem.load(itemSection));
            }
            return new Shop(name, items);
        };
    }

    @Override
    public Consumer<List<Shop>> saveProcess() {
        return null;
    }
}
