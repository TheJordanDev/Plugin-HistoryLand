package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.WarpCommand;
import fr.thejordan.historyland.helper.FileHelper;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.warps.WarpList;
import fr.thejordan.historyland.object.warps.WarpListConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WarpManager extends AbstractManager {

    private static WarpManager instance;
    public static WarpManager instance() { return instance; }

    private final WarpListConfig warpListConfig;

    private Map<String, WarpList> warpLists;
    public Map<String, WarpList> warpLists() { return warpLists; }


    public WarpManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.warpListConfig = new WarpListConfig(plugin);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.warpLists = this.warpListConfig.load().stream().collect(
                Collectors.toMap(WarpList::getName, Function.identity())
        );
        if (warpLists.isEmpty()) { createPlaceholder(); onReload(); }
    }

    @Override
    public void onReload() {
        super.onReload();
        this.warpLists = this.warpListConfig.load().stream().collect(
                Collectors.toMap(WarpList::getName, Function.identity())
        );
        if (warpLists.isEmpty()) { createPlaceholder(); onReload(); }
    }

    private void createPlaceholder() {
        InputStreamReader defaultWarpFile = FileHelper.getStreamFromResource("defaultWarpList.yml");
        File defaultWarpInFile = new File(plugin().getDataFolder(),"/warps/warps.yml");
        if (defaultWarpFile != null) {
            try { YamlConfiguration.loadConfiguration(defaultWarpFile).save(defaultWarpInFile); }
            catch (Exception ignored) {}
        }
    }

    @Override
    public List<Listener> listeners() {
        return null;
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new WarpCommand());
    }

}
