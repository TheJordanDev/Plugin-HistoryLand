package fr.thejordan.historyland.object.shop;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.AbstractConfigFolder;
import fr.thejordan.historyland.object.collectible.PlayersItems;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class BoughtItemsConfig extends AbstractConfigFolder<PlayersItems> {

    public BoughtItemsConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public File folder() {
        return new File(getPlugin().getDataFolder(),"boughtItems");
    }

    @Override
    public boolean filter(File dir, String name) {
        return Helper.getUUID(name.split("\\.")[0]).isPresent() && name.endsWith(".yml");
    }

    @Override
    public List<String> blacklist() {
        return List.of();
    }

    @Override
    public String name() {
        return "Objets des joueurs";
    }

    @Override
    public Function<YamlConfiguration, PlayersItems> loadProcess() {
        return config -> {
            UUID uuid = TT.UUID.uuid(config.getString("uuid"));
            if (uuid == null) return null;
            List<String> items = config.getStringList("items");
            return new PlayersItems(uuid,items);
        };
    }

    @Override
    public Consumer<List<PlayersItems>> saveProcess() {
        return playersItems -> {
            for (PlayersItems item : playersItems) {
                File file = new File(folder(), item.uuid().toString()+".yml");
                YamlConfiguration config = config(file);
                config.set("uuid",TT.UUID.string(item.uuid()));
                config.set("items",item.items());
                try { config.save(file); }
                catch (IOException e) { throw new UncheckedIOException(e); }
            }
        };
    }
}
