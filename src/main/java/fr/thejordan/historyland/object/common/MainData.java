package fr.thejordan.historyland.object.common;

import fr.thejordan.historyland.Historyland;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

public class MainData {

    @Getter @Setter private String currency;
    @Getter @Setter private int activityGiftDelay;
    @Getter @Setter private double activityGiftAmount;
    @Getter @Setter private String resourcePackUrl;

    private MainData(String currency, int activityGiftDelay, double activityGiftAmount, String resourcePackUrl) {
        this.currency = currency;
        this.activityGiftDelay = activityGiftDelay;
        this.activityGiftAmount = activityGiftAmount;
        this.resourcePackUrl = resourcePackUrl;
    }

    public static MainData load(FileConfiguration configuration) {
        String currency = configuration.getString("currency", "€");
        int activity_gift_delay = configuration.getInt("activity_gift_delay", 10);
        double activity_gift_amount = configuration.getDouble("activity_gift_amount", 10);
        String resource_pack_url = configuration.getString("resourcepack-url", "");
        return new MainData(currency, activity_gift_delay, activity_gift_amount, resource_pack_url);
    }

    public void save(FileConfiguration config) {
        config.set("currency", currency);
        config.set("activity_gift_delay", activityGiftDelay);
        config.set("activity_gift_amount", activityGiftAmount);
        config.set("resourcepack-url", resourcePackUrl);
        Historyland.instance().saveConfig();
    }
}
