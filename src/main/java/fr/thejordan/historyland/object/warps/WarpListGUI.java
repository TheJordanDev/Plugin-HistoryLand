package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.manager.WarpManager;
import fr.thejordan.historyland.object.AbstractGUI;
import fr.thejordan.historyland.object.BItem;
import fr.thejordan.historyland.object.Keys;
import fr.thejordan.historyland.object.Translator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class WarpListGUI extends AbstractGUI {

    public WarpListGUI(Player player) {
        super(player);
    }

    @Override
    public String id() {
        return "warplist";
    }

    @Override
    public String title() {
        return "§a"+Translator.translate(getPlayer(),"menu_warplist_title");
    }

    @Override
    public Integer rows() {
        return 3;
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        for (WarpList list : WarpManager.instance().warpLists().values()) {
            slots.put(list.getSlot(), list.getIcon());
        }
        return slots;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        BItem item = BItem.of(stack);
        if (!item.hData(Keys.WARPLIST_ICON_KEY, PersistentDataType.STRING)) return;
        String id = item.gData(Keys.WARPLIST_ICON_KEY, PersistentDataType.STRING);
        WarpList list = WarpManager.instance().warpLists().get(id);
        if (list == null) return;
        new WarpTPGUI(getPlayer(), list).open();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
