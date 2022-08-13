package fr.thejordan.historyland.object.common;

import fr.thejordan.historyland.Historyland;
import org.bukkit.NamespacedKey;

public class Keys {

    //PREFERENCES
    public static final NamespacedKey PREFERENCE_KEY = new NamespacedKey(Historyland.instance(), "preference_key");

    //COLLECTIBLES
    public static final NamespacedKey COLLECTIBLE_KEY = new NamespacedKey(Historyland.instance(), "collectible");
    public static final NamespacedKey CUSTOM_ARMOR_KEY = new NamespacedKey(Historyland.instance(), "armor");
    public static final NamespacedKey COLLECTIBLE_FILTER_KEY = new NamespacedKey(Historyland.instance(), "menu_collectible_filter");

    //SHOP
    public static final NamespacedKey SHOP = new NamespacedKey(Historyland.instance(), "shop");
    public static final NamespacedKey SHOP_ITEM_KEY = new NamespacedKey(Historyland.instance(), "shopItem");
    public static final NamespacedKey SHOP_OWNER_KEY = new NamespacedKey(Historyland.instance(), "shopOwnerStick");

    //SEAT
    public static final NamespacedKey SEAT_KEY = new NamespacedKey(Historyland.instance(), "seat");
    public static final NamespacedKey PLAYER_LAST_LOC_KEY = new NamespacedKey(Historyland.instance(), "last-loc");

    //LANGUAGE
    public static final NamespacedKey LANGUAGE_ICON_KEY = new NamespacedKey(Historyland.instance(), "language-icon");

    //WARPS
    public static final NamespacedKey WARPLIST_ICON_KEY = new NamespacedKey(Historyland.instance(), "warplist-icon");
    public static final NamespacedKey WARP_ICON_KEY = new NamespacedKey(Historyland.instance(), "warp-icon");
    public static final NamespacedKey WARP_ICON_SLOT_KEY = new NamespacedKey(Historyland.instance(), "warp-icon-slot");

    //UI
    public static final NamespacedKey NEXT_PAGE_KEY = new NamespacedKey(Historyland.instance(), "next-page");
    public static final NamespacedKey PREV_PAGE_KEY = new NamespacedKey(Historyland.instance(), "prev-page");
    public static final NamespacedKey BACK_PAGE_KEY = new NamespacedKey(Historyland.instance(), "back-page");

    //JETS
    public static final NamespacedKey JET_KEY = new NamespacedKey(Historyland.instance(), "jet");

    //ASC
    public static final NamespacedKey ASCMD_KEY = new NamespacedKey(Historyland.instance(), "asc");
    public static final NamespacedKey ASC_CMD_KEY = new NamespacedKey(Historyland.instance(), "asccommand");
    public static final NamespacedKey ASCMD_ISSERVER_KEY = new NamespacedKey(Historyland.instance(), "isServer");
}
