package fr.thejordan.historyland.object;

import fr.thejordan.historyland.Historyland;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractConfigFolder<T> {

    @Getter private final JavaPlugin plugin;

    public AbstractConfigFolder(JavaPlugin plugin) {
        this.plugin = plugin;
        create(true);
    }

    public abstract File folder();
    public abstract boolean filter(File dir, String name);
    public abstract List<String> blacklist();
    public abstract String name();

    public abstract Function<YamlConfiguration,T> loadProcess();
    public abstract Consumer<List<T>> saveProcess();

    public void create(boolean sendCreationMessage) {
        if (folder().exists()) return;
        folder().mkdirs();
        if (sendCreationMessage) Historyland.log("Dossier de configuration \""+name()+"\" créé !");
    }

    public File[] getFiles() {
        if (!folder().exists()) create(true);
        if (blacklist() == null)
            return folder().listFiles((this::filter));
        else
            return folder().listFiles(((dir, name) -> !blacklist().contains(name) && filter(dir,name)));
    }

    public YamlConfiguration config(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try { file.createNewFile(); }
            catch (IOException e) { e.printStackTrace(); }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public List<T> load() {
        List<T> returned = new ArrayList<>();
        for (File file : getFiles()) {
            YamlConfiguration configuration = config(file);
            returned.add(loadProcess().apply(configuration));
        }
        return returned;
    }

    public void save(List<T> objects) {
        saveProcess().accept(objects);
    }

}
