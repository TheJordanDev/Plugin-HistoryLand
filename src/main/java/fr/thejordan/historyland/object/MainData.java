package fr.thejordan.historyland.object;

import org.bukkit.configuration.file.FileConfiguration;

public record MainData(
        String currency,
        int activityGiftDelay,
        double activityGiftAmount,
        String resourcePackUrl
) {

    public static MainData load(FileConfiguration configuration) {
        String currency = configuration.getString("currency", "€");
        int activity_gift_delay = configuration.getInt("activity_gift_delay", 10);
        double activity_gift_amount = configuration.getDouble("activity_gift_amount", 10);
        String resource_pack_url = configuration.getString("resourcepack-url", "");
        return new MainData(currency, activity_gift_delay, activity_gift_amount, resource_pack_url);
    }

}
