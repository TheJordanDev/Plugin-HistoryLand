package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.command.SitCommand;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.Translator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatManager extends AbstractManager {

    private static SeatManager instance;
    public static SeatManager instance() { return instance; }

    private final Map<Location, ArmorStand> used = new HashMap<>();
    public Map<Location, ArmorStand> used() { return used; }

    public SeatManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Map.Entry<Location, ArmorStand> entry : used.entrySet()) {
            entry.getValue().eject();
            entry.getValue().remove();
        }
        used.clear();
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(new SitCommand());
    }

    public boolean isSeat(ArmorStand stand) {
        return stand.getPersistentDataContainer().has(Keys.SEAT_KEY, PersistentDataType.STRING);
    }

    public void sit(Player player) {
        if (!player.isOnGround()) return;
        if (player.getVehicle() != null) return;
        Block used;
        BlockData bData = player.getLocation().getBlock().getBlockData();
        if (bData instanceof Stairs || bData instanceof Slab) used = player.getLocation().getBlock();
        else used = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        createSeat(player, used);
    }

    private ArmorStand createSeat(Player player, Block block) {
        Location tpTo = player.getLocation().clone();
        Location location = block.getLocation().clone().add(0.5D, -0.7, 0.5D);
        if (block.getBlockData() instanceof Stairs stairs)
            location = location.add(Helper.getStairDirection(stairs)).add(0,-0.5,0);
        else if (block.getBlockData() instanceof Slab)
            location = location.add(0,-0.5,0);
        else if(block.getType() == Material.CHIPPED_ANVIL)
            location = location.add(0,-0.5,0);
        World world = location.getWorld();
        ArmorStand stand = world.spawn(location, ArmorStand.class, (arm)->{
            if (block.getBlockData() instanceof Stairs stairs) arm.setRotation(Helper.getYawFromStair(stairs), 0);
            else if (block.getType() == Material.CHIPPED_ANVIL) arm.setRotation(Helper.getYawComplex(((Directional)block.getBlockData()).getFacing().getOppositeFace()),0);
            else arm.setRotation(player.getLocation().getYaw(), 0);
            arm.setVisible(false);
            arm.setGravity(false);
            arm.setInvulnerable(true);
            arm.getPersistentDataContainer().set(Keys.SEAT_KEY, PersistentDataType.STRING, TT.Location.string(block.getLocation()));
            arm.getPersistentDataContainer().set(Keys.PLAYER_LAST_LOC_KEY, PersistentDataType.STRING, TT.Location.string(tpTo));
        });
        stand.addPassenger(player);
        used.put(block.getLocation(), stand);
        return stand;
    }

    @EventHandler
    public void onPlayerClickOnSeat(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block.getType() != Material.CHIPPED_ANVIL) return;
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        if (block.getLocation().distance(player.getLocation()) > 4) Translator.send(player, "seat_too_far");
        else if (player.getVehicle() != null) Translator.send(player,"seat_already_sit");
        else if (used().containsKey(block.getLocation())) Translator.send(player, "seat_someone_already");
        else createSeat(player, block);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDismounted() instanceof ArmorStand stand)) return;
        dismount(player, stand);
    }

    private void dismount(Player player, ArmorStand armorStand) {
        if (!isSeat(armorStand)) return;
        Location location = TT.Location.location(armorStand.getPersistentDataContainer().get(Keys.SEAT_KEY, PersistentDataType.STRING));
        Block seat = location.getBlock();
        armorStand.eject();
        if (armorStand.getPersistentDataContainer().has(Keys.PLAYER_LAST_LOC_KEY, PersistentDataType.STRING)) {
            Location tp = TT.Location.location(armorStand.getPersistentDataContainer().get(Keys.PLAYER_LAST_LOC_KEY, PersistentDataType.STRING));
            tp.setYaw(player.getEyeLocation().getYaw());
            tp.setPitch(player.getEyeLocation().getPitch());
            player.teleport(tp);
        } else
            player.teleport(player.getLocation().clone().add(0.0,0.7D,0.0));
        armorStand.remove();
        used().remove(seat.getLocation());
    }
}
