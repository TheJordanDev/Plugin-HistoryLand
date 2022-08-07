package fr.thejordan.historyland.object.preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPreferences {

    private final UUID uuid;
    private final Map<Preference, Object> preferences;
    public UUID uuid() { return uuid; }
    public Map<Preference, Object> preferences() { return preferences; }

    public PlayerPreferences(UUID uuid) {
        this.uuid = uuid;
        this.preferences = Arrays.stream(Preference.values()).collect(HashMap::new, (map, preference) -> map.put(preference, preference.defaultValue()), Map::putAll);
    }

    public PlayerPreferences(UUID uuid, Map<Preference, Object> preferences) {
        this.uuid = uuid;
        this.preferences = preferences;
    }

    public Object getPreference(Preference preference) {
        return preferences.getOrDefault(preference, preference.defaultValue());
    }



}
