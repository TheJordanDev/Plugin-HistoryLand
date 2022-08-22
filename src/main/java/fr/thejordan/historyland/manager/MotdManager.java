package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.MotdCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.motd.Motd;
import fr.thejordan.historyland.object.motd.MotdConfig;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MotdManager extends AbstractManager {

    private static MotdManager instance;
    public static MotdManager instance() { return instance; }

    private final MotdConfig motdConfig;

    @Getter private Motd motd = new Motd();


    public MotdManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.motdConfig = new MotdConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.motd = motdConfig.load();
    }

    @Override
    public void onReload() {
        super.onReload();
        this.motd = motdConfig.load();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        motdConfig.save(motd);
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new MotdCommand());
    }

    @EventHandler
    public void onPin(ServerListPingEvent event) {
        event.setMotd(motd.format());
    }

}
