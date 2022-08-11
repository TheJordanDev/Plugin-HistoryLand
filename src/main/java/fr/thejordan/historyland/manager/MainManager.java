package fr.thejordan.historyland.manager;

import com.earth2me.essentials.Essentials;
import fr.thejordan.historyland.command.BroadcastCommand;
import fr.thejordan.historyland.command.ReloadCommand;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.AbstractManager;
import fr.thejordan.historyland.object.common.MainData;
import fr.thejordan.historyland.scheduler.ActivityGiftScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class MainManager extends AbstractManager {

    private static MainManager instance;
    public static MainManager instance() { return instance; }

    private Essentials essentials;
    public Essentials essentials() { return essentials; }

    private MainData data;
    public MainData data() { return data; }

    private final Map<UUID, Long> lastGifts = new HashMap<>();
    public Map<UUID, Long> lastGifts() { return lastGifts; }

    private final ActivityGiftScheduler activityGiftScheduler;
    public ActivityGiftScheduler activityGiftScheduler() { return activityGiftScheduler; }

    public MainManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
        this.activityGiftScheduler = new ActivityGiftScheduler();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        essentials = JavaPlugin.getPlugin(Essentials.class);
        plugin().reloadConfig();
        data = MainData.load(plugin().getConfig());
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective lang = scoreboard.getObjective("LANG");
        if (lang == null) lang = scoreboard.registerNewObjective("LANG", "dummy", "LANG", RenderType.INTEGER);
        Bukkit.getOnlinePlayers().forEach(p ->
                lastGifts.put(p.getUniqueId(), Calendar.getInstance().getTimeInMillis())
        );
        activityGiftScheduler.start();
    }

    @Override
    public void onReload() {
        super.onReload();
        plugin().reloadConfig();
        data = MainData.load(plugin().getConfig());
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of(
                new BroadcastCommand(),
                new ReloadCommand()
        );
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        lastGifts.put(player.getUniqueId(), Calendar.getInstance().getTimeInMillis());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        lastGifts.remove(player.getUniqueId());
    }

    @EventHandler
    public void onCommandProp(PlayerCommandSendEvent event) {
        event.getCommands().removeIf((s) -> s.contains("historyland:"));
    }

    @EventHandler
    public void onInspect(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().name().startsWith("RIGHT_CLICK")) return;
        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack == null) return;
        if (stack.getType() != Material.KNOWLEDGE_BOOK) return;
    }

    @EventHandler
    public void onFlowerPotInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().name().startsWith("RIGHT_CLICK")) return;
        if (!event.hasBlock()) return;
        if (event.getPlayer().isOp()) return;
        Material mat = event.getClickedBlock().getType();
        if (mat == Material.FLOWER_POT || mat.name().startsWith("POTTED_")) {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        Player player = event.getPlayer();
        if (player.isOp()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFramePop(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) return;
        if (!(event.getDamager() instanceof Player player)) return;
        if (player.isOp()) return;
        event.setCancelled(true);
    }

}
