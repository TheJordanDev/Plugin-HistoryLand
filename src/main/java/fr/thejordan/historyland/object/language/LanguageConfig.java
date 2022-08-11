package fr.thejordan.historyland.object.language;

import fr.thejordan.historyland.object.common.AbstractConfigFolder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class LanguageConfig extends AbstractConfigFolder<Language> {

    public LanguageConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File folder() {
        return new File(getPlugin().getDataFolder(), "languages");
    }

    @Override
    public boolean filter(File dir, String name) {
        return name.endsWith(".yml");
    }

    @Override
    public List<String> blacklist() {
        return List.of("spoken.yml","regions.yml");
    }

    @Override
    public String name() {
        return "Languages";
    }

    @Override
    public Function<YamlConfiguration, Language> loadProcess() {
        return Language::load;
    }

    @Override
    public Consumer<List<Language>> saveProcess() {
        return (ls)->{};
    }
}
