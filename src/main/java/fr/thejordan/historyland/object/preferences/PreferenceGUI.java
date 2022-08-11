package fr.thejordan.historyland.object.preferences;

import fr.thejordan.historyland.manager.PreferencesManager;
import fr.thejordan.historyland.object.common.*;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PreferenceGUI extends AbstractPagedGUI {

    public final PlayerPreferences preferences;
    public final Partition<Preference> pages;

    public PreferenceGUI(Player player) {
        super(player);
        this.preferences = PreferencesManager.instance().getPreferences(player.getUniqueId());
        this.pages = new Partition<>(new ArrayList<>(List.of(Preference.values())), 27);
    }

    @Override
    public String id() {
        return "preferences";
    }

    @Override
    public String title() {
        return Translator.translate(getPlayer(),"menu_preferences_title");
    }

    @Override
    public Integer rows() {
        return 4;
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        if (pages.size() != 0 && getPage() <= pages.size()) {
            for (int i = 0; i < pages.get(getPage() - 1).size(); i++) {
                Preference preference = pages.get(getPage() - 1).get(i);
                boolean state = (boolean) preferences.getPreference(preference);
                slots.put(i, preference.toItem(getPlayer(), state));
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
        if (item.hData(Keys.PREFERENCE_KEY, PersistentDataType.STRING)) {
            if (pages.size() != 0) {
                Preference preference = pages.get(getPage() - 1).get(event.getRawSlot());
                boolean state = (boolean) preferences.getPreference(preference);
                preferences.preferences().put(preference, !state);
                if (state)
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
                else
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
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
        PreferencesManager.instance().preferences().put(getPlayer().getUniqueId(), preferences);
    }
}
