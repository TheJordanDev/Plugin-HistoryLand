package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Warp {

    @Getter private final WarpList parent;
    @Getter private final Integer slot;

    @Getter private final String name;
    @Getter private final WarpType type;
    @Getter private final List<String> description;
    @Getter private final Location location;

    public Warp(WarpList parent,Integer slot, String name, WarpType type, List<String> description, Location location) {
        this.parent = parent;
        this.slot = slot;
        this.name = name;
        this.type = type;
        this.description = description;
        this.location = location;
    }

    public ItemStack icon() {
        return BItem.of(type.getMaterial())
                .displayName("§r"+name)
                .lore(description)
                .sData(Keys.WARPLIST_ICON_KEY, PersistentDataType.STRING, parent.getName())
                .sData(Keys.WARP_ICON_SLOT_KEY, PersistentDataType.INTEGER, getSlot())
                .unbreakable()
                .stack();
    }

}