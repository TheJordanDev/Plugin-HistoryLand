package fr.thejordan.historyland.object.realtime;

import fr.thejordan.historyland.Historyland;
import lombok.Getter;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;

public class RealTimeScheduler extends BukkitRunnable {

    @Getter
    private final Map<World, Long> worlds;

    public RealTimeScheduler(Map<World, Long> worlds) {
        this.worlds = worlds;
    }

    public void add(World world, Long time) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setStorm(false);
        world.setThundering(false);
        this.worlds.put(world, time);
    }

    public void remove(World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        this.worlds.remove(world);
    }

    public boolean isOn(World world) {
        return this.worlds.containsKey(world);
    }

    public RealTimeScheduler start() {
        for (World world : worlds.keySet()) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setStorm(false);
            world.setThundering(false);
        }
        runTaskTimer(Historyland.instance(), 0L, 72L);
        return this;
    }

    @Override
    public void run() {
        Set<Map.Entry<World, Long>> _worlds = worlds.entrySet();
        for (Map.Entry<World, Long> world : _worlds) {
            Long time = worlds.get(world.getKey());
            world.getKey().setTime(time);
            worlds.put(world.getKey(), time + 1);
        }
    }

}
