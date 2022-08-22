package fr.thejordan.historyland.object.motd;

import fr.thejordan.historyland.object.common.AbstractConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MotdConfig extends AbstractConfigFile<Motd> {

    public MotdConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File file() {
        return new File(getPlugin().getDataFolder(), "motd.yml");
    }

    @Override
    public String name() {
        return "MOTD";
    }

    @Override
    public Function<YamlConfiguration, Motd> loadProcess() {
        ArrayList<String> def = new ArrayList<>(List.of(Bukkit.getMotd().split("\n")));
        return (conf)-> new Motd(
                conf.getString("line1", (def.size() >= 1) ? def.get(0) : ""),
                conf.getString("line2", (def.size() >= 2) ? def.get(1) : "")
        );
    }

    @Override
    public Consumer<Motd> saveProcess(YamlConfiguration configuration) {
        return (motd)-> {
            configuration.set("line1", motd.getLine1());
            configuration.set("line2", motd.getLine2());
        };
    }
}
