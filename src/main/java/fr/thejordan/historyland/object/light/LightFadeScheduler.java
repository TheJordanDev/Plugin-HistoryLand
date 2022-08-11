package fr.thejordan.historyland.object.light;

import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.helper.Helper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LightFadeScheduler extends BukkitRunnable {

    @Getter public final static List<LightFadeScheduler> schedulers = new ArrayList<>();

    @Getter private final Location location;
    @Getter @Setter private int varLevel;
    @Getter private final int currentLevel,targetLevel;
    @Getter private final boolean waterlogged;

    public LightFadeScheduler(Location location,boolean waterlogged,int currentLevel, int targetLevel) {
        this.location = location;
        this.currentLevel = currentLevel;
        this.waterlogged = waterlogged;
        this.varLevel = currentLevel;
        this.targetLevel = targetLevel;
    }

    @Override
    public void run() {
        if (varLevel == targetLevel) { schedulers.remove(this); cancel(); return; }
        if (varLevel > targetLevel) varLevel -= 1;
        else varLevel += 1;
        Helper.setLight(location,varLevel,waterlogged);
    }

    public static void fade(Location location,boolean waterlogged,int currentLevel,int targetLevel,double delay) {
        LightFadeScheduler lightFadeScheduler = new LightFadeScheduler(location,waterlogged, currentLevel, targetLevel);
        int max = Math.max(currentLevel,targetLevel);
        int min = Math.min(currentLevel,targetLevel);
        long period = (long) ((delay/(max-min)));
        schedulers.add(lightFadeScheduler);
        lightFadeScheduler.runTaskTimer(Historyland.instance(),0L,period);
    }

}
