package fr.thejordan.historyland.scheduler;

import com.earth2me.essentials.User;
import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.MainManager;
import fr.thejordan.historyland.manager.PreferencesManager;
import fr.thejordan.historyland.object.common.Translator;
import fr.thejordan.historyland.object.preferences.Preference;
import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ActivityGiftScheduler extends BukkitRunnable {

    public ActivityGiftScheduler start() {
        this.runTaskTimer(Historyland.instance(), 0, 20);
        return this;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = MainManager.instance().essentials().getUser(player);
            if (user.isAfk()) {
                MainManager.instance().lastGifts().remove(player.getUniqueId());
                continue;
            }
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            Long lastTime = MainManager.instance().lastGifts().getOrDefault(player.getUniqueId(),currentTime);
            if (!MainManager.instance().lastGifts().containsKey(player.getUniqueId()))
                MainManager.instance().lastGifts().put(player.getUniqueId(), currentTime);
            if ((currentTime - lastTime)/1000 > MainManager.instance().data().activityGiftDelay()) {
                MainManager.instance().lastGifts().put(player.getUniqueId(), currentTime);
                try {user.setMoney(user.getMoney()
                        .add(BigDecimal.valueOf(MainManager.instance().data().activityGiftAmount()))
                );
                } catch (MaxMoneyException ignored) {}
                boolean showMessage = (boolean) PreferencesManager.instance().getPreference(player.getUniqueId(), Preference.SHOW_ACTIVITY_REWARD);
                if (showMessage) {
                    double amount = MainManager.instance().data().activityGiftAmount();
                    String currency = MainManager.instance().data().currency();
                    String message = Translator.translate(player, "activity_gift_message",
                            Map.of("amount", String.valueOf(amount), "currency", currency));
                    String tooltip = Translator.translate(player, "click_to_open_preferences");
                    ComponentBuilder navBar = new ComponentBuilder();
                    Helper.addHoverCommandMessage(navBar, message, "/preferences", List.of(tooltip));
                    player.spigot().sendMessage(navBar.create());
                }
            }
        }
    }

}
