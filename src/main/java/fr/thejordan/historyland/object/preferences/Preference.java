package fr.thejordan.historyland.object.preferences;

import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.Translator;
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
        return BItem.of((state)? Material.ENDER_EYE : Material.ENDER_PEARL)
                .displayName(((state)?"§a✔":"§c✖")+" "+Translator.translate(player, key))
                .sData(Keys.PREFERENCE_KEY, PersistentDataType.STRING, key).stack();
    }

}
