package fr.thejordan.historyland.object.preferences;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.AbstractConfigFolder;
import fr.thejordan.historyland.object.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreferenceConfig extends AbstractConfigFolder<PlayerPreferences> {

    public PreferenceConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File folder() {
        return new File(getPlugin().getDataFolder(),"preferences");
    }

    @Override
    public boolean filter(File dir, String name) {
        return name.endsWith(".yml");
    }

    @Override
    public List<String> blacklist() {
        return null;
    }

    @Override
    public String name() {
        return "Preferences";
    }

    @Override
    public Function<YamlConfiguration, PlayerPreferences> loadProcess() {
        return config -> {
            UUID uuid = TT.UUID.uuid(config.getString("uuid"));
            ConfigurationSection prefs = config.getConfigurationSection("preferences");
            Map<Preference, Object> preferences = prefs.getValues(false).entrySet().stream()
                    .filter(entry -> Helper.isValueOfEnum(Preference.class, entry.getKey()).isPresent())
                    .map(entry -> {
                        Preference preference = Preference.valueOf(entry.getKey());
                        return new Pair<>(preference, entry.getValue());
                    }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            return new PlayerPreferences(uuid, preferences);
        };
    }

    @Override
    public Consumer<List<PlayerPreferences>> saveProcess() {
        return (preferences) -> {
            for (PlayerPreferences preference : preferences) {
                File file = new File(folder(), preference.uuid().toString()+".yml");
                YamlConfiguration config = config(file);
                config.set("uuid",TT.UUID.string(preference.uuid()));
                preference.preferences().forEach(
                        (pref, value) -> config.set("preferences."+pref.name(), (value == null) ? pref.defaultValue() : value)
                );
                try { config.save(file); }
                catch (IOException e) { throw new UncheckedIOException(e); }
            }
        };
    }
}
