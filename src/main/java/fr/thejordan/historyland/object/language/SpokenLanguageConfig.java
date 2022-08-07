package fr.thejordan.historyland.object.language;

import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.AbstractConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpokenLanguageConfig extends AbstractConfigFile<Map<UUID,String>> {

    public SpokenLanguageConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(),"languages/spoken.yml");
    }

    @Override
    public String name() {
        return "Spoken Language";
    }

    @Override
    public Function<YamlConfiguration, Map<UUID, String>> loadProcess() {
        return (conf) -> conf.getKeys(false).stream().collect(Collectors.toMap(TT.UUID::uuid, str->conf.getString(str,"default")));
    }

    @Override
    public Consumer<Map<UUID, String>> saveProcess(YamlConfiguration config) {
        return (spoken) -> spoken.forEach((uuid, language) -> config.set(TT.UUID.string(uuid), language));
    }
}
