package fr.thejordan.historyland.object.realtime;

import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class RealTimeConfig extends AbstractConfigFile<Map<World, Long>> {

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(),"realtime.yml");
    }

    @Override
    public String name() {
        return "RealTime";
    }

    public RealTimeConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Function<YamlConfiguration, Map<World, Long>> loadProcess() {
        return (conf)->{
            Map<World, Long> map = new HashMap<>();
            if (!conf.isConfigurationSection("worlds")) return map;
            ConfigurationSection section = conf.getConfigurationSection("worlds");
            for (String name : section.getKeys(false)) {
                World world = Bukkit.getWorld(name);
                if (world == null) continue;
                map.put(world, section.getLong(name));
            }
            return map;
        };
    }

    @Override
    public Consumer<Map<World, Long>> saveProcess(YamlConfiguration configuration) {
        return (map)->{
            configuration.set(".", null);
            ConfigurationSection section = configuration.createSection("worlds");
            map.forEach((w, t) -> section.set(w.getName(), t));
        };
    }


}
