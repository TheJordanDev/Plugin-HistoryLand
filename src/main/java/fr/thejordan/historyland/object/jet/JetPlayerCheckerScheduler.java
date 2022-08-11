package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.manager.JetManager;
import org.bukkit.scheduler.BukkitRunnable;

public class JetPlayerCheckerScheduler extends BukkitRunnable {

    @Override
    public void run() {
        for (JetCategory category : JetManager.instance().getJets().values()) {
            for (Jet jet : category.getJets().values()) {
                jet.setActive(jet.arePlayersNearby(jet.getActivationRange()));
            }
        }
    }

}
