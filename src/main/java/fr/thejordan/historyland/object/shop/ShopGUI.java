package fr.thejordan.historyland.object.shop;

import fr.thejordan.historyland.object.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class ShopGUI extends AbstractPagedGUI {

    private final Shop shop;
    private final Partition<ShopItem> pages;

    public ShopGUI(Player player, Shop shop) {
        super(player);
        this.shop = shop;
        this.pages = new Partition<>(shop.getItems(), 27);
    }

    @Override
    public String id() {
        return "shop";
    }

    @Override
    public String title() {
        return Translator.translate("menu_shop_title",getPlayer()).replaceAll("%shop%",shop.getName());
    }

    @Override
    public Integer rows() {
        return 5;
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        if (pages.size() != 0 && getPage() <= pages.size()) {
            for (int i = 0; i < pages.get(getPage() - 1).size(); i++) {
                slots.put(9 + i, pages.get(getPage() - 1).get(i).shopDisplay(getPlayer()));
            }
        }
        if (getPage() != 1) slots.put(39, Items.prevArrow(getPlayer()));
        if (pages.size() > 1 && pages.size() >= getPage()) slots.put(41, Items.nextArrow(getPlayer()));
        return slots;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        BItem item = BItem.of(stack);
        if (item.hData(Keys.SHOP_ITEM_KEY, PersistentDataType.STRING)) {
            if (pages.size() != 0) {
                ShopItem sItem = pages.get(getPage() - 1).get(event.getRawSlot() - 9);
                sItem.buy(player);
            }
        } else if (item.hData(Keys.NEXT_PAGE_KEY, PersistentDataType.STRING)) {
            setPage(getPage() + 1);
        } else if (item.hData(Keys.PREV_PAGE_KEY, PersistentDataType.STRING)) {
            setPage(getPage() - 1);
        }
        refresh();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

}
