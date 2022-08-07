package fr.thejordan.historyland.object;

import fr.thejordan.historyland.object.shop.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class Items {

    public static ItemStack nextArrow(Player player) {
        return Skin.Skins.arrow_right.asSkin().asHead((m)-> BItem.of(m)
                .displayName(Translator.translate(player, "gui.next"))
                .sData(Keys.NEXT_PAGE_KEY, PersistentDataType.STRING, "next"));
    }

    public static ItemStack prevArrow(Player player) {
        return Skin.Skins.arrow_left.asSkin().asHead((m)-> BItem.of(m)
                .displayName(Translator.translate(player, "gui.prev"))
                .sData(Keys.PREV_PAGE_KEY, PersistentDataType.STRING, "prev"));
    }

    public static ItemStack backArrow(Player player) {
        return Skin.Skins.arrow_backwards.asSkin().asHead((m)-> BItem.of(m)
                .displayName(Translator.translate(player, "gui.back"))
                .sData(Keys.BACK_PAGE_KEY, PersistentDataType.STRING, "back"));
    }

    public static ItemStack shopOwnerStick(Shop shop) {
        return BItem.of(Material.BLAZE_ROD)
                .displayName("§b§lSHOP - §6§l" + shop.getName())
                .sData(Keys.SHOP_OWNER_KEY, PersistentDataType.STRING, shop.getName())
                .stack();
    }

}
