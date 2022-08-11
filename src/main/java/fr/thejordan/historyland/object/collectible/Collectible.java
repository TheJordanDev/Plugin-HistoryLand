package fr.thejordan.historyland.object.collectible;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import fr.thejordan.historyland.manager.CollectibleManager;
import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.Skin;
import lombok.Getter;
import net.minecraft.world.item.ItemArmor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Collectible {

    public static Collectible unknownItem = new Collectible(
            "item_unknown",
            "§cERROR",
            Material.BARRIER,
            1,
            Collections.emptyList(),
            0,
            0,
            ItemType.OTHER,
            new ItemData()
    );

    public static Comparator<Collectible> collectibleTypeComparator = Comparator.comparingInt(o -> o.type.sortPrio);
    public static Comparator<Collectible> collectibleNameComparator = Comparator.comparing(o -> ChatColor.stripColor(o.name));

    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final Material material;
    @Getter
    private final Integer amount;
    @Getter
    private final List<String> lore;
    @Getter
    private final Integer modelID;
    @Getter
    private final Integer damage;
    @Getter
    private final ItemType type;
    @Getter
    private final ItemData data;

    public Collectible(String id, String name, Material material, Integer amount, List<String> lore, Integer modelID, Integer damage, ItemType type, ItemData data) {
        this.id = id;
        this.name = "§r§f" + name;
        this.material = material;
        this.amount = amount;
        this.lore = lore;
        this.modelID = modelID;
        this.damage = damage;
        this.type = type;
        this.data = data;
    }

    public static Collectible load(String id, ConfigurationSection section) {
        String name = "ITEM";
        Material material = Material.GRASS_BLOCK;
        List<String> lore = new ArrayList<>();
        int amount = 1;
        int modelID = 0;
        int damage = 0;
        ItemType type = ItemType.OTHER;
        ItemData data = new ItemData();
        for (String key : section.getKeys(false)) {
            switch (key) {
                case "name" -> name = section.getString(key, name);
                case "material" -> material = TT.Material.material(section.getString(key, material.name()));
                case "lore" -> lore.addAll(section.getStringList(key).stream().map(s -> "§r" + s).toList());
                case "modelID" -> modelID = section.getInt(key, 0);
                case "damage" -> damage = section.getInt(key, 0);
                case "amount" -> amount = section.getInt(key, 1);
                case "type" -> type = ItemType.fromString(section.getString(key, ItemType.OTHER.toString()));
                case "skin" -> data.setSkin(section.getString(key, null));
                case "color" -> {
                    String colorStr = section.getString(key,"rgb(255,255,255)");
                    if (Helper.isRGB(colorStr)) data.setColor(Helper.parseRGB(colorStr));
                }
            }
        }
        return new Collectible(id, name, material, amount, lore, modelID, damage, type, data);
    }

    public ItemStack toItemStack() {
        BItem item = BItem.of(material).displayName(name)
                .amount(amount).lore(lore).unbreakable()
                .flags(ItemFlag.values()).damage(damage)
                .model(modelID).sData(Keys.COLLECTIBLE_KEY, PersistentDataType.STRING, getId());
        if (item.getItemMeta() instanceof LeatherArmorMeta)
            item.color(this.data.getColor());
        if (isCustomArmor(item.stack()))
            item.sData(Keys.CUSTOM_ARMOR_KEY, PersistentDataType.STRING, getType().name());
        if (item.getType() == Material.PLAYER_HEAD)
            item.skin(this.data.getSkinTexture());
        return item.stack();
    }

    public boolean isCustomArmor(ItemStack stack) {
        if (!CollectibleManager.isCollectible(stack)) return false;
        return (!(CraftItemStack.asNMSCopy(stack).getItem() instanceof ItemArmor) && !getType().equals(Collectible.ItemType.OTHER));
    }

    public static class ItemData {

        @Getter //Placeholder
        private Skin skinTexture = Skin.Skins.DEFAULT.asSkin();

        @Getter
        private Color color = Color.BLUE;

        public ItemData setSkin(String skin) {
            return setSkin(new Skin(skin));
        }

        public ItemData setSkin(Skin skin) {
            if (skin != null) this.skinTexture = skin;
            return this;
        }

        public ItemData setColor(Color color) {
            if (color != null) this.color = color;
            return this;
        }

    }
    public enum ItemType {
        NONE(-1,"menu_collectible_filter_none"),
        HEAD(1,"menu_collectible_filter_hat"),
        CHESTPLATE(1,"menu_collectible_filter_chestplate"),
        LEGGINGS(1,"menu_collectible_filter_leggings"),
        BOOTS(1,"menu_collectible_filter_boots"),
        OTHER(1,"menu_collectible_filter_other");

        private final int sortPrio;
        public final String display;

        ItemType(int sortPrio,String name) {
            this.sortPrio = sortPrio;
            this.display = name;
        }

        public static ItemType fromString(String str) {
            try { return valueOf(str.toUpperCase()); }
            catch (Exception e) { return OTHER; }
        }

        public ItemType prev() { return values()[(ordinal() - 1  + values().length) % values().length]; }

        public ItemType next() { return values()[(ordinal() + 1) % values().length]; }

    }

}