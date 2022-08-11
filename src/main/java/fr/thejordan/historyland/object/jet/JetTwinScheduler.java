package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.Historyland;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class JetTwinScheduler extends BukkitRunnable {

    private final Long PERIOD = 10L;

    @Getter private final Jet jet;
    @Getter private final Vector target;
    @Getter private final Vector offset;
    @Getter private final Double ticks;
    @Getter private final Vector diff;

    public JetTwinScheduler(Jet jet, double secs, Vector target) {
        this.jet = jet;
        this.target = target;
        this.ticks = PERIOD.doubleValue()*secs;
        this.diff = target.clone().subtract(jet.getVector());
        this.offset = diff.clone().multiply(1/ticks);
        runTaskTimer(Historyland.instance(),0L,2L);
    }

    @Override
    public void run() {
        if (finished()) { stop(); return; }
        jet.getVector().add(offset);
    }

    public void stop() {
        jet.setVector(target);
        jet.setTwinScheduler(null);
        cancel();
    }

    private boolean finished() {
        boolean isX = false;
        boolean isY = false;
        boolean isZ = false;
        if ((diff.getX() == 0) ||
                (diff.getX() < 0 && jet.getVector().getX() <= target.getX()) ||
                (diff.getX() > 0 && jet.getVector().getX() >= target.getX())
        )
            isX = true;

        if ((diff.getY() == 0) ||
                (diff.getY() < 0 && jet.getVector().getY() <= target.getY()) ||
                (diff.getY() > 0 && jet.getVector().getY() >= target.getY())

        )
            isY = true;
        if ((diff.getZ() == 0) ||
                (diff.getZ() < 0 && jet.getVector().getZ() <= target.getZ()) ||
                (diff.getZ() > 0 && jet.getVector().getZ() >= target.getZ())
        )
            isZ = true;
        return (isX && isY && isZ);
    }

}
