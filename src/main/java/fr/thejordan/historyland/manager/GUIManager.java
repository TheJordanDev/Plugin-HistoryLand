package fr.thejordan.historyland.manager;

import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.AbstractGUI;
import fr.thejordan.historyland.object.AbstractManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GUIManager extends AbstractManager {

    private static GUIManager instance;
    public static GUIManager instance() { return instance; }

    private final HashMap<UUID,AbstractGUI> guis = new HashMap<>();
    public HashMap<UUID,AbstractGUI> guis() { return guis; }

    public GUIManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    @Override
    public List<Listener> listeners() {
        return List.of(this);
    }

    @Override
    public List<AbstractCommand> commands() {
        return List.of();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        onClick(event);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        onClose(event);
    }

    private void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof AbstractGUI.GUIHolder)) return;
        AbstractGUI.GUIHolder holder = (AbstractGUI.GUIHolder) event.getInventory().getHolder();
        AbstractGUI inventory = guis().get(event.getWhoClicked().getUniqueId());
        if (inventory == null) return;
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        event.setCancelled(true);
        inventory.onClick(event);
    }

    private void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractGUI.GUIHolder)) return;
        event.getInventory().getHolder();
        AbstractGUI inventory = guis().get(event.getPlayer().getUniqueId());
        if (inventory == null) return;
        inventory.onClose(event);
        guis().remove(event.getPlayer().getUniqueId());
    }

}
