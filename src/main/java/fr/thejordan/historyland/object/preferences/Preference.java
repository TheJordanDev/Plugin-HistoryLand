package fr.thejordan.historyland.object.preferences;

import fr.thejordan.historyland.object.BItem;
import fr.thejordan.historyland.object.Keys;
import fr.thejordan.historyland.object.Translator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public enum Preference {

    SHOW_ACTIVITY_REWARD("preferences_show_gift_message", true);

    private final String key;
    public String key() { return key; }
    private final boolean defaultValue;
    public boolean defaultValue() { return defaultValue; }

    Preference(String key, boolean defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public ItemStack toItem(Player player, boolean state) {
        return BItem.of((state)? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .displayName(((state)?"§a✔":"§c✖")+" "+Translator.translate(player, key))
                .sData(Keys.PREFERENCE_KEY, PersistentDataType.STRING, key).stack();
    }

}
