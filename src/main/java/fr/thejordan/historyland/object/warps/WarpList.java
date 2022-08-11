package fr.thejordan.historyland.object.warps;

import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import lombok.Getter;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class WarpList {

    @Getter private final String name;
    private final ItemStack icon;
    @Getter private final int slot;

    public ItemStack getIcon() {
        return BItem.of(icon.clone())
                .displayName("§r"+icon.getItemMeta().getDisplayName())
                .flags(ItemFlag.values())
                .sData(Keys.WARPLIST_ICON_KEY, PersistentDataType.STRING, name)
                .unbreakable()
                .stack();
    }

    @Getter private final Map<Integer,Map<Integer, Warp>> children;

    public WarpList(String name, int slot, ItemStack icon, Map<Integer,Map<Integer, Warp>> children) {
        this.name = name;
        this.slot = slot;
        this.icon = icon;
        this.children = children;
    }

}

