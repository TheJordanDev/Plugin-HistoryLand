package fr.thejordan.historyland.object.language;

import fr.thejordan.historyland.manager.LanguageManager;
import fr.thejordan.historyland.object.AbstractGUI;
import fr.thejordan.historyland.object.BItem;
import fr.thejordan.historyland.object.Keys;
import fr.thejordan.historyland.object.Translator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Optional;

public class LanguageChooserGUI extends AbstractGUI {

    @Override
    public String id() {
        return "language_chooser";
    }

    @Override
    public String title() {
        return Translator.translate(getPlayer(),"menu_language_title");
    }

    @Override
    public Integer rows() {
        return 5;
    }

    public LanguageChooserGUI(Player player) {
        super(player);
    }

    @Override
    public Map<Integer, ItemStack> slots() {
        Map<Integer, ItemStack> slots = background();
        Language[] languages = LanguageManager.instance().languages().values().toArray(new Language[0]);
        for (int i = 0; i < LanguageManager.instance().languages().size(); i++) {
            Language language = languages[i];
            BItem icon = BItem.of(language.icon());
            if (LanguageManager.instance().getLanguage(getPlayer()) == language) {
                icon.glow();
                icon.prefix("§a§l");
            } else {
                icon.prefix("§7§l");
            }
            slots.put(i, icon.stack());
        }
        return slots;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        BItem item = BItem.of(event.getCurrentItem());
        if (item.getType() == Material.AIR) return;
        if (!item.hData(Keys.LANGUAGE_ICON_KEY, PersistentDataType.STRING)) return;
        String langId = item.gData(Keys.LANGUAGE_ICON_KEY, PersistentDataType.STRING);
        Optional<Language> languageOpt = LanguageManager.instance().getLanguage(langId);
        if (languageOpt.isEmpty()) return;
        Language language = languageOpt.get();
        if (language == LanguageManager.instance().getLanguage(getPlayer())) return;
        LanguageManager.instance().setLanguage(getPlayer(), language);
        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
        open();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }


}
