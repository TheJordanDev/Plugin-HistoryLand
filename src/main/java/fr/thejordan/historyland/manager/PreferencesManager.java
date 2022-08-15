package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.PreferencesCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.preferences.PlayerPreferences;
import fr.thejordan.historyland.object.preferences.Preference;
import fr.thejordan.historyland.object.preferences.PreferenceConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PreferencesManager extends AbstractManager {

    private static PreferencesManager instance;
    public static PreferencesManager instance() { return instance; }

    private final PreferenceConfig preferenceConfig;

    private Map<UUID, PlayerPreferences> preferences = new HashMap<>();
    public Map<UUID, PlayerPreferences> preferences() { return preferences; }

    public PreferencesManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.preferenceConfig = new PreferenceConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.preferences = preferenceConfig.load().stream().collect(HashMap::new, (map, preference) -> map.put(preference.uuid(), preference), Map::putAll);
        Bukkit.getOnlinePlayers().stream().filter(player -> !preferences.containsKey(player.getUniqueId())).forEach(player ->
                preferences.put(player.getUniqueId(), new PlayerPreferences(player.getUniqueId()))
        );
    }

    @Override
    public void onReload() {
        this.preferences = preferenceConfig.load().stream().collect(HashMap::new, (map, preference) -> map.put(preference.uuid(), preference), Map::putAll);
        Bukkit.getOnlinePlayers().stream().filter(player -> !preferences.containsKey(player.getUniqueId())).forEach(player ->
                preferences.put(player.getUniqueId(), new PlayerPreferences(player.getUniqueId()))
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();
        preferenceConfig.save(new ArrayList<>(preferences.values()));
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new PreferencesCommand());
    }

    public PlayerPreferences getPreferences(UUID uuid) {
        if (!preferences.containsKey(uuid)) preferences.put(uuid, new PlayerPreferences(uuid, new HashMap<>()));
        return preferences.get(uuid);
    }

    public Object getPreference(UUID uuid, Preference preference) {
        if (!preferences.containsKey(uuid)) preferences.put(uuid, new PlayerPreferences(uuid, new HashMap<>()));
        return preferences.get(uuid).preferences().getOrDefault(preference, preference.defaultValue());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!preferences.containsKey(event.getPlayer().getUniqueId()))
            preferences.put(event.getPlayer().getUniqueId(), new PlayerPreferences(event.getPlayer().getUniqueId()));
    }

}
