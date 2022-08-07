package fr.thejordan.historyland.object;

import org.bukkit.configuration.file.FileConfiguration;

public record MainData(String currency, int activityGiftDelay, double activityGiftAmount) {

    public static MainData load(FileConfiguration configuration) {
        String currency = configuration.getString("currency", "€");
        int activity_gift_delay = configuration.getInt("activity_gift_delay", 10);
        double activity_gift_amount = configuration.getDouble("activity_gift_amount", 10);
        return new MainData(currency, activity_gift_delay, activity_gift_amount);
    }

}
