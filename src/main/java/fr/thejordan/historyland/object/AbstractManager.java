package fr.thejordan.historyland.object;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class AbstractManager implements Listener {

    private final JavaPlugin plugin;
    public JavaPlugin plugin() { return plugin; }

    public AbstractManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        if (listeners() != null) listeners().forEach((l) -> plugin.getServer().getPluginManager().registerEvents(l, plugin));
        if (commands() != null) commands().forEach((c) -> c.register(plugin));
    }

    public void onDisable() {

    }
    public void onReload() {

    }

    public abstract List<Listener> listeners();
    public abstract List<AbstractCommand> commands();

}
