package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.RealTimeCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.realtime.RealTimeConfig;
import fr.thejordan.historyland.object.realtime.RealTimeScheduler;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealTimeManager extends AbstractManager {

    private static RealTimeManager instance;
    public static RealTimeManager instance() { return instance; }

    private Map<World, Long> worlds = new HashMap<>();

    @Getter private RealTimeConfig realTimeConfig;
    @Getter private RealTimeScheduler realTime;

    public RealTimeManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.realTimeConfig = new RealTimeConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.worlds = realTimeConfig.load();
        this.realTime = new RealTimeScheduler(worlds);
        this.realTime.start();
    }

    @Override
    public void onReload() {
        super.onReload();
        this.worlds = realTimeConfig.load();
        this.realTime.getWorlds().clear();
        this.realTime.getWorlds().putAll(worlds);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        realTimeConfig.save(worlds);
    }

    @Override
    public List<Listener> listeners() {
        return null;
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(
                new RealTimeCommand()
        );
    }
}
