package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.manager.JetManager;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.Map;

public class JetCategory {

    @Getter private final String id;
    @Getter private final Map<String,Jet> jets;

    public JetCategory(String id, Map<String,Jet>jets) {
        this.id = id;
        this.jets = jets;
    }

    public void move(Vector vector, Integer secondes) {
        if (secondes == 0) secondes = 1;
        int ticks = 10 * secondes;
        for (Jet jet : jets.values()) {
            Vector diff = vector.clone().subtract(jet.getVector());
            Vector offset = diff.clone().multiply(1 / ticks);
            jet.setTwinScheduler(new JetTwinScheduler(jet, secondes, vector));
        }
    }

    public void remove(Jet jet) {
        if (jet.getTwinScheduler() != null) jet.getTwinScheduler().cancel();
        getJets().remove(jet.getName());
        if (getJets().size() == 0) JetManager.instance().remove(this);
    }

    public void setRange(double range) {
        for (Jet jet : getJets().values()) {
            jet.setActivationRange(range);
        }
    }

}
