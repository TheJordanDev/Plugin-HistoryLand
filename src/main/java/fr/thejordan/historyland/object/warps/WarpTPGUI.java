package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.object.common.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class WarpTPGUI extends AbstractPagedGUI {

    private final WarpList parent;
    private final Map<Integer, Map<Integer, Warp>> pages;

    public WarpTPGUI(Player player, WarpList parent) {
        super(player);
        this.parent = parent;
        this.pages = parent.getChildren();
    }

    @Override
    public String id() {
        return "warptp";
    }

    @Override
    public String title() {
        return "§a"+ Translator.translate(getPlayer(),"menu_warptp_title")
                .replace("%list%", BItem.of(parent.getIcon()).displayName());
    }

    @Override
    public Integer rows() {
        return 4;
    }

    public boolean isMultiPage() {
        return parent.getChildren().size() > 1;
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        if (pages.containsKey(getPage()))
            for (Map.Entry<Integer, Warp> entry : pages.get(getPage()).entrySet())
                slots.put(entry.getKey(), entry.getValue().icon());
        if (isMultiPage()) {
            if (getPage() > 1) slots.put(30, Items.prevArrow(getPlayer()));
            if (getPage() < pages.size()) slots.put(32,Items.nextArrow(getPlayer()));
        }
        slots.put(31, Items.backArrow(getPlayer()));
        return slots;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        BItem item = BItem.of(stack);
        if (item.hData(Keys.WARP_ICON_SLOT_KEY, PersistentDataType.INTEGER)) {
            Warp warp = pages.get(getPage()).get(event.getSlot());
            if (warp != null) getPlayer().teleport(warp.getLocation());
            getPlayer().closeInventory();
        } else if (item.hData(Keys.BACK_PAGE_KEY, PersistentDataType.STRING)) {
            new WarpListGUI(getPlayer()).open();
        } else if (item.hData(Keys.PREV_PAGE_KEY, PersistentDataType.STRING)) {
            if (getPage() > 1) setPage(getPage() - 1);
        } else if (item.hData(Keys.NEXT_PAGE_KEY, PersistentDataType.STRING)) {
            if (getPage() < pages.size()) setPage(getPage() + 1);
        }
        refresh();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }
}
