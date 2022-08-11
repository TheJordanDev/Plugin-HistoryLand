package fr.thejordan.historyland.object.language;

import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PhrasesPerRegionsConfig extends AbstractConfigFile<Map<String, List<String>>> {

    public PhrasesPerRegionsConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(),"languages/regions.yml");
    }

    @Override
    public String name() {
        return "PhrasesPerRegion";
    }

    @Override
    public Function<YamlConfiguration, Map<String, List<String>>> loadProcess() {
        return (conf) -> conf.getKeys(false).stream().collect(Collectors.toMap(str->str, conf::getStringList));
    }

    @Override
    public Consumer<Map<String, List<String>>> saveProcess(YamlConfiguration config) {
        return (p)->{};
    }
}
