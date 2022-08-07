package fr.thejordan.historyland.object;

import fr.thejordan.historyland.Historyland;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractConfigFile<T> {

    public abstract File file();
    public abstract String name();

    @Getter
    private final JavaPlugin plugin;

    public AbstractConfigFile(JavaPlugin plugin) {
        this.plugin = plugin;
        if (createWhenNotFound()) create(true);
    }

    public boolean createWhenNotFound() {
        return true;
    }

    public abstract Function<YamlConfiguration, T> loadProcess();
    public abstract Consumer<T> saveProcess(YamlConfiguration configuration);

    public boolean create(boolean sendCreationMessage) {
        if (file().exists()) return true;
        file().getParentFile().mkdirs();
        try {
            file().createNewFile();
            if (sendCreationMessage) Historyland.log("Configuration \"" + name() + "\" créée !");
            return true;
        } catch (IOException e) { Historyland.wrn("Erreur à la création de la configuration \""+name()+"\" !"); }
        return false;
    }

    public YamlConfiguration config() {
        if (!file().exists()) if (!create(true)) return null;
        return YamlConfiguration.loadConfiguration(file());
    }

    public T load() {
        YamlConfiguration configuration = config();
        if (configuration == null) return null;
        if (loadProcess() == null) return null;
        try { return loadProcess().apply(config()); }
        catch (Exception e) { e.printStackTrace(); return null;}
    }

    public void save(T obj) {
        YamlConfiguration configuration = config();
        if (configuration == null) return;
        if (saveProcess(configuration) == null) return;
        try {
            saveProcess(configuration).accept(obj);
            configuration.save(file());
        } catch (Exception e) { e.printStackTrace(); }
    }

}
