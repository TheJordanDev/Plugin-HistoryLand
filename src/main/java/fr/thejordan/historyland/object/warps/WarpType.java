package fr.thejordan.historyland.object.warps;

import lombok.Getter;
import org.bukkit.Material;

public enum WarpType {

    GRANDSPECTACLE("Grand Spectacle", Material.RED_CONCRETE),
    SPECTACLE("Spectacle",Material.BLUE_CONCRETE),
    ANIMATION("Animation",Material.PURPLE_CONCRETE),
    ATTRACTION("Attraction",Material.PURPLE_CONCRETE),
    HOTEL("Hôtel",Material.YELLOW_CONCRETE),
    RESTAURANT_BOUTIQUE_LIEUDIT("Restaurant / Boutique / Lieu-dit",Material.WHITE_CONCRETE);

    @Getter private final String name;
    @Getter private final Material material;

    WarpType(String name, Material material) {
        this.name = name;
        this.material = material;
    }

}
