package fr.thejordan.historyland.object.collectible;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.CollectibleManager;
import fr.thejordan.historyland.object.common.*;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectibleGUI extends AbstractPagedGUI {

    @Getter private Collectible.ItemType type = Collectible.ItemType.NONE;
    @Getter private PlayersItems items;
    @Getter private Partition<Collectible> pages;

    public CollectibleGUI(Player player) {
        super(player);
        this.items = CollectibleManager.instance().getOwned().getOrDefault(player.getUniqueId(), new PlayersItems(player.getUniqueId(), List.of()));
        ArrayList<Collectible> collectibles = items.items().stream().map(CollectibleManager::getByID).collect(Collectors.toCollection(ArrayList::new));
        this.pages = new Partition<>(collectibles, 36);
    }

    @Override
    public String id() {
        return "collectibles";
    }

    @Override
    public String title() {
        return Translator.translate(getPlayer(), "menu_collectible_title");
    }

    @Override
    public Integer rows() {
        return 6;
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        if (pages.size() > 0 && getPage() <= pages.size()) {
            List<Collectible> collectibles = pages.get(getPage() - 1);
            switch (type) {
                case HEAD -> collectibles = collectibles.stream().filter(c -> c.getType() == Collectible.ItemType.HEAD).collect(Collectors.toList());
                case CHESTPLATE -> collectibles = collectibles.stream().filter(c -> c.getType() == Collectible.ItemType.CHESTPLATE).collect(Collectors.toList());
                case LEGGINGS -> collectibles = collectibles.stream().filter(c -> c.getType() == Collectible.ItemType.LEGGINGS).collect(Collectors.toList());
                case BOOTS -> collectibles = collectibles.stream().filter(c -> c.getType() == Collectible.ItemType.BOOTS).collect(Collectors.toList());
                case OTHER -> collectibles = collectibles.stream().filter(c -> c.getType() == Collectible.ItemType.OTHER).collect(Collectors.toList());
            }
            for (int i = 0; i < collectibles.size(); i++) {
                ItemStack stack = collectibles.get(i).toItemStack();
                BItem item = BItem.of(stack);
                if (new Armor(getPlayer()).wearsItem(stack)) item.glow();
                slots.put(i, item.stack());
            }
            if (getPage() > 1) slots.put(48, Items.prevArrow(getPlayer()));
            slots.put(49, Items.collectibleFilter(getPlayer(),type));
            if (pages.size() > 1 && pages.size() >= getPage()) slots.put(50, Items.nextArrow(getPlayer()));
        }
        return slots;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack stack = event.getCurrentItem();
        if (stack == null) return;
        BItem item = BItem.of(stack);
        if (item.hData(Keys.COLLECTIBLE_KEY, PersistentDataType.STRING)) {
            if (pages.size() > 0) {
                String id = item.gData(Keys.COLLECTIBLE_KEY, PersistentDataType.STRING);
                Collectible cosmetic = CollectibleManager.getByID(id);
                if (cosmetic == null) return;
                Armor armor = new Armor(getPlayer());
                ItemStack clicked = Helper.removeAllEnchants(stack);
                ItemStack currentArmor = Helper.removeAllEnchants(armor.getFromType(cosmetic.getType()));
                if (currentArmor.isSimilar(clicked)) {
                    armor.setFromType(new ItemStack(Material.AIR), cosmetic.getType());
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
                } else {
                    armor.setFromType(clicked, cosmetic.getType());
                    getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                }
                armor.apply();
            }
        } else if (item.hData(Keys.COLLECTIBLE_FILTER_KEY, PersistentDataType.STRING)) {
            if (event.isRightClick()) type = type.next();
            else type = type.prev();
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
