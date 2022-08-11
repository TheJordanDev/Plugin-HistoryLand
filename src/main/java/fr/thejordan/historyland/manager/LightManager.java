package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.LightCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class LightManager extends AbstractManager {

    private static LightManager instance;
    public static LightManager instance() { return instance;}

    public LightManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    @Override
    public List<Listener> listeners() {
        return null;
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(
                new LightCommand()
        );
    }
}
