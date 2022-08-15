package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.JetCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.ValueWError;
import fr.thejordan.historyland.object.jet.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JetManager extends AbstractManager {

    private static JetManager instance;
    public static JetManager instance() { return instance;}

    private final JetConfig config;

    private final JetScheduler jetScheduler;
    public JetScheduler getJetScheduler() { return jetScheduler; }

    private final JetPlayerCheckerScheduler playerCheckerScheduler;
    public JetPlayerCheckerScheduler getPlayerCheckerScheduler() { return playerCheckerScheduler; }

    private Map<String, JetCategory> jets = new HashMap<>();
    public Map<String, JetCategory> getJets() { return jets; }

    public JetManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.config = new JetConfig(plugin);
        this.jetScheduler = new JetScheduler();
        this.playerCheckerScheduler = new JetPlayerCheckerScheduler();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        jets = config.load().stream().collect(Collectors.toMap(JetCategory::getId, Function.identity()));
        jetScheduler.runTaskTimer(plugin(),0L, 2L);
        playerCheckerScheduler.runTaskTimer(plugin(),0L, 20L);
    }

    @Override
    public void onReload() {
        jets = config.load().stream().collect(Collectors.toMap(JetCategory::getId, Function.identity()));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        jets.forEach((id, cat) -> cat.getJets().forEach((name, jet) -> {
            if (jet.getTwinScheduler() != null) jet.getTwinScheduler().cancel();
        }));
        config.save(new ArrayList<>(jets.values()));
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new JetCommand());
    }

    public Jet create(String id, String name, Location location, Vector vector, Material material) {
        if (!jets.containsKey(id)) jets.put(id,new JetCategory(id,new HashMap<>()));
        Jet jet = new Jet(id,name,location,vector,material);
        this.getJets().get(id).getJets().put(jet.getName(), jet);
        return jet;
    }

    public void move(Jet jet, Vector vector, Integer seconds) {
        if (seconds == 0) seconds = 1;
        jet.setTwinScheduler(new JetTwinScheduler(jet,seconds,vector));
    }

    public ValueWError getJet(String name) {
        int separator = StringUtils.countMatches(name,":");
        if (separator == 0) {
            if (!JetManager.instance.getJets().containsKey(name)) return new ValueWError(null, "§cLa catégorie "+name+" n'éxiste pas !");
            return new ValueWError(JetManager.instance.getJets().get(name),"");
        } else if (separator == 1) {
            String[] nameSplit = name.split(":");
            String id = nameSplit[0];
            if (nameSplit.length == 1) return new ValueWError(null, "§cVeuillez préciser un jet après ':' !");
            if (!JetManager.instance.getJets().containsKey(id)) return new ValueWError(null, "§cLa catégorie "+id+" n'éxiste pas !");
            JetCategory category = JetManager.instance.getJets().get(id);
            String jet = nameSplit[1];
            if (!category.getJets().containsKey(jet)) return new ValueWError(null, "§cLe jet "+jet+" n'éxiste pas !");
            return new ValueWError(category.getJets().get(jet), "§cLa catégorie "+name+" n'éxiste pas !");
        }
        return new ValueWError(null, "§cErreur");
    }

    public void remove(JetCategory category) {
        for (Jet jet : category.getJets().values()) {
            if (jet.getTwinScheduler() != null) jet.getTwinScheduler().cancel();
        }
        getJets().remove(category.getId());

        File file = new File(config.folder(),category.getId()+".yml");
        if (file.exists()) file.delete();
    }

    @EventHandler
    public void onFallingBlockTurnsSolid(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock fallingBlock)) return;
        if (fallingBlock.getPersistentDataContainer().has(Keys.JET_KEY, PersistentDataType.STRING)) {
            event.setCancelled(true);
        }
    }

}
