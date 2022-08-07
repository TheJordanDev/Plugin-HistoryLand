package fr.thejordan.historyland.object;

import fr.thejordan.historyland.manager.GUIManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGUI {

    public static ItemStack defaultBG = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    static {
        ItemMeta meta = defaultBG.getItemMeta();
        meta.setDisplayName(" ");
        defaultBG.setItemMeta(meta);
    }

    @Getter private final Player player;
    @Getter private Inventory inventory;

    public abstract String id();
    public abstract String title();
    public abstract Integer rows();

    public AbstractGUI(Player player) {
        this.player = player;
    }

    public void open() {
        this.inventory = Bukkit.createInventory(
                new GUIHolder(id()), rows() * 9, title()
        );
        refresh();
        player.openInventory(inventory);
        GUIManager.instance().guis().put(player.getUniqueId(), this);
    }

    public void refresh() {
        slots().forEach(inventory::setItem);
    }

    public abstract Map<Integer, ItemStack> slots();

    public abstract void onClick(InventoryClickEvent event);

    public abstract void onClose(InventoryCloseEvent event);

    public Map<Integer, ItemStack> background(ItemStack stack) {
        Map<Integer, ItemStack> returned = new HashMap<>();
        for (int i = 0; i < rows() * 9; i++) returned.put(i, stack);
        return returned;
    }

    public Map<Integer, ItemStack> background() {
        return background(defaultBG);
    }

    public static class GUIHolder implements InventoryHolder {
        @Getter
        private final String id;
        public GUIHolder(String id) { this.id = id; }

        @Override
        public Inventory getInventory() { return null; }

    }

}
