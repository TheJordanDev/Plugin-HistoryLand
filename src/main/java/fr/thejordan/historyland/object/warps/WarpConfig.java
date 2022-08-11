package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class WarpConfig extends AbstractConfigFile<Map<Integer, Map<Integer,Warp>>> {

    private final WarpList parent;

    public WarpConfig(JavaPlugin plugin, WarpList parent) {
        super(plugin);
        this.parent = parent;
    }

    @Override
    public boolean createWhenNotFound() {
        return false;
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(),"/warps/"+parent.getName()+".yml");
    }

    @Override
    public String name() {
        return parent.getName()+" Warps";
    }

    @Override
    public Function<YamlConfiguration, Map<Integer, Map<Integer, Warp>>> loadProcess() {
        return configuration -> {
            Map<Integer, Map<Integer, Warp>> warps = new HashMap<>();
            for (String _page : configuration.getKeys(false)) {
                if (!Helper.isInt(_page)) continue;
                Integer page = Integer.parseInt(_page);
                ConfigurationSection section = configuration.getConfigurationSection(_page);
                if (section == null) continue;
                Map<Integer, Warp> _warps = new HashMap<>();
                for (String _slot : section.getKeys(false)) {
                    if (!Helper.isInt(_slot)) continue;
                    Integer slot = Integer.parseInt(_slot);
                    ConfigurationSection warpSection = section.getConfigurationSection(_slot);
                    if (warpSection == null) continue;
                    String name = warpSection.getString("name", "Warp");
                    List<String> lore = warpSection.getStringList("description");
                    Location location = TT.Location.location(warpSection.getString("location"));
                    Optional<WarpType> _type = Helper.isValueOfEnum(WarpType.class, warpSection.getString("type", "ANIMATION"));
                    WarpType type = _type.orElse(WarpType.ANIMATION);
                    _warps.put(slot, new Warp(parent, slot, name, type, lore, location));
                }
                warps.put(page, _warps);
            }
            return warps;
        };
    }

    public void createPlaceholder() {
        InputStreamReader defaultShopFile = FileHelper.getStreamFromResource("defaultWarp.yml");
        File defaultShopInFile = new File(getPlugin().getDataFolder(),"/warps/"+parent.getName()+".yml");
        if (defaultShopFile != null && !defaultShopInFile.exists()) {
            try { YamlConfiguration.loadConfiguration(defaultShopFile).save(defaultShopInFile); }
            catch (Exception ignored) {}
        }
    }

    @Override
    public Consumer<Map<Integer, Map<Integer, Warp>>> saveProcess(YamlConfiguration configuration) {
        return null;
    }

}
