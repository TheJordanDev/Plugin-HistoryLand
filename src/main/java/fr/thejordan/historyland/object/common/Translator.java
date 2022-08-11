package fr.thejordan.historyland.object.common;

import fr.thejordan.historyland.manager.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Translator {

    public static String translate(Player player, String key) {
        return translate(player, key, Map.of());
    }

    public static String translate(Player player, String key, Map<String, String> params) {
        String msg = LanguageManager.instance().getLanguage(player).getMessage(key);
        for (Map.Entry<String, String> entries : params.entrySet()) {
            msg = msg.replace("%" + entries.getKey() + "%", entries.getValue());
        }
        return msg;
    }

    public static void send(Player player, String key) {
        send(player,key,Map.of());
    }
    public static void send(Player player, String key, Map<String, String> params) {
        player.sendMessage(translate(player, key, params));
    }

    public static void broadcast(String message, Map<String, String> params, List<Player> excluded) {
        List<Player> targets = new ArrayList<>(Bukkit.getOnlinePlayers());
        targets.removeAll(excluded);
        targets.forEach(t->Translator.send(t, message, params));
    }

    public static boolean canTranslate(Player player, String key) {
        return LanguageManager.instance().getLanguage(player).messages().containsKey(key);
    }

}
