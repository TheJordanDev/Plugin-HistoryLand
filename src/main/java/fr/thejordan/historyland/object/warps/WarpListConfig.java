package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.object.AbstractConfigFile;
import fr.thejordan.historyland.object.BItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class WarpListConfig extends AbstractConfigFile<List<WarpList>> {

    public WarpListConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean createWhenNotFound() {
        return false;
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(), "/warps/warps.yml");
    }

    @Override
    public String name() {
        return "Warps";
    }

    @Override
    public Function<YamlConfiguration, List<WarpList>> loadProcess() {
        return (configuration) -> {
            List<WarpList> warpLists = new ArrayList<>();
            for (String key : configuration.getKeys(false)) {
                ConfigurationSection section = configuration.getConfigurationSection(key);
                if (section == null) continue;
                int slot = section.getInt("slot",0);
                ItemStack icon = new ItemStack(Material.GRASS_BLOCK);
                if (section.isConfigurationSection("icon"))
                    icon = BItem.of(section.getConfigurationSection("icon")).stack();
                WarpList warpList = new WarpList(key, slot, icon,new HashMap<>());
                WarpConfig config = new WarpConfig(getPlugin(), warpList);
                if (!config.file().exists()) config.createPlaceholder();
                warpList.getChildren().putAll(config.load());
                warpLists.add(warpList);
            }
            return warpLists;
        };
    }

    @Override
    public Consumer<List<WarpList>> saveProcess(YamlConfiguration configuration) {
        return null;
    }
}
