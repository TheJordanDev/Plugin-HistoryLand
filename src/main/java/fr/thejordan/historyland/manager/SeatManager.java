package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractManager;
import fr.thejordan.historyland.object.Keys;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;

public class SeatManager extends AbstractManager {

    private static SeatManager instance;
    public static SeatManager instance() { return instance; }

    public SeatManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of();
    }

    public boolean isSeat(ArmorStand stand) {
        return stand.getPersistentDataContainer().has(Keys.SEAT_KEY, PersistentDataType.STRING);
    }

    public void createSeat(Player player, Block block) {
        Location location = block.getLocation().clone().add(0.5D, 0, 0.5D);
        World world = location.getWorld();
        ArmorStand stand = world.spawn(location, ArmorStand.class, (arm)->{
            arm.setVisible(false);
            arm.setGravity(false);
            arm.setInvulnerable(true);
            arm.getPersistentDataContainer().set(Keys.SEAT_KEY, PersistentDataType.STRING, "true");
            arm.getPersistentDataContainer().set(Keys.PLAYER_LAST_LOC_KEY, PersistentDataType.STRING, TT.Location.string(player.getLocation()));
        });
        stand.addPassenger(player);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDismounted() instanceof ArmorStand armorStand)) return;

    }
}
