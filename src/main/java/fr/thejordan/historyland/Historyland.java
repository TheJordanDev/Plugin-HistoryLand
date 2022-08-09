package fr.thejordan.historyland;

import fr.thejordan.historyland.manager.*;
import fr.thejordan.historyland.object.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public final class Historyland extends JavaPlugin {

    public static final String PREFIX = "[HistoryLand] ";
    public static final String GAME_PREFIX = "§6[§eHistoryLand§6] ";

    private static Historyland instance;
    public static Historyland instance() { return instance;}

    private List<? extends AbstractManager> managers;

    public static void log(Object msg) { instance.getLogger().log(Level.INFO, PREFIX+msg.toString()); }
    public static void wrn(Object msg) { instance.getLogger().log(Level.WARNING, PREFIX+msg.toString()); }
    public static void err(Object msg) { instance.getLogger().log(Level.SEVERE, PREFIX+msg.toString()); }

    @Override
    public void onEnable() {
        instance = this;
        managers = List.of(
                new MainManager(this),
                new LanguageManager(this),
                new PreferencesManager(this),
                new ResourcePackManager(this),
                new SeatManager(this),
                new GUIManager(this),
                new CollectibleManager(this),
                new ShopManager(this),
                new WarpManager(this)
        );
        saveDefaultConfig();
        managers.forEach(AbstractManager::onEnable);
    }

    @Override
    public void onDisable() {
        managers.forEach(AbstractManager::onDisable);
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void onReload() {
        managers.forEach(AbstractManager::onReload);
    }
}
