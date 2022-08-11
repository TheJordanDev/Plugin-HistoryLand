package fr.thejordan.historyland.object.collectible;

import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CollectibleConfig extends AbstractConfigFile<List<Collectible>> {

    public CollectibleConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(), "collectibles.yml");
    }

    @Override
    public String name() {
        return "Collectibles";
    }

    @Override
    public Function<YamlConfiguration, List<Collectible>> loadProcess() {
        return (configuration) -> {
            List<Collectible> collectibles = new ArrayList<>();
            for (String id : configuration.getKeys(false)) {
                if (configuration.isConfigurationSection(id))
                    collectibles.add(Collectible.load(id,configuration.getConfigurationSection(id)));
            }
            return collectibles;
        };
    }

    @Override
    public Consumer<List<Collectible>> saveProcess(YamlConfiguration configuration) {
        return null;
    }
}
