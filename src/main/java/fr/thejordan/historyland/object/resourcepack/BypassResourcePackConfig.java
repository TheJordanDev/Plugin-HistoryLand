package fr.thejordan.historyland.object.resourcepack;

import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BypassResourcePackConfig extends AbstractConfigFile<List<UUID>> {

    public BypassResourcePackConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(), "bypass-resource-pack.yml");
    }

    @Override
    public String name() {
        return "ResourcePack Bypass";
    }

    @Override
    public Function<YamlConfiguration, List<UUID>> loadProcess() {
        return (config) -> {
            List<UUID> players = config.getStringList("players")
                    .stream().map(TT.UUID::uuid)
                    .collect(Collectors.toList());
            return players;
        };
    }

    @Override
    public Consumer<List<UUID>> saveProcess(YamlConfiguration configuration) {
        return (config)-> configuration.set("players", config.stream().map(TT.UUID::string).collect(Collectors.toList()));
    }
}
