package fr.thejordan.historyland.object.language;

import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.Skin;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public record Language(@Getter String id, @Getter int objective_value, @Getter String name, @Getter Skin skin,
                       @Getter Map<String, String> messages) {

    public static Language load(YamlConfiguration configuration) {
        String id = configuration.getString("id", "lang");
        int objective = configuration.getInt("lang_id", -1);
        String name = configuration.getString("name", "Language");
        Skin skin = (configuration.isString("skin")) ? TT.Skin.skin(configuration.getString("skin")) : Skin.Skins.DEFAULT.asSkin();
        Map<String, String> messages = new HashMap<>();
        configuration.getConfigurationSection("strings").getKeys(false).forEach(key -> messages.put(key, configuration.getString("strings." + key)));
        return new Language(id, objective, name, skin, messages);
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, key);
    }

    public ItemStack icon() {
        return skin.asHead((i)-> BItem.of(i)
                .displayName(name)
                .sData(Keys.LANGUAGE_ICON_KEY, PersistentDataType.STRING, id())
        );
    }

}
