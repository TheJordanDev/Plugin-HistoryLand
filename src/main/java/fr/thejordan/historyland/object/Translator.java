package fr.thejordan.historyland.object;

import fr.thejordan.historyland.manager.LanguageManager;
import org.bukkit.entity.Player;

public class Translator {

    public static String translate(String key, Player player, String... args) {
        return translate(player, key, args);
    }
    public static String translate(Player player, String key, String... args) {
        String msg = translate(player, key);
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replace("{" + i + "}", args[i]);
            }
        }
        return msg;
    }

    public static String translate(String key, Player player) {
        return translate(player, key);
    }
    public static String translate(Player player, String key) {
        return LanguageManager.instance().getLanguage(player).getMessage(key);
    }

    public static boolean canTranslate(Player player, String key) {
        return LanguageManager.instance().getLanguage(player).messages().containsKey(key);
    }

}
