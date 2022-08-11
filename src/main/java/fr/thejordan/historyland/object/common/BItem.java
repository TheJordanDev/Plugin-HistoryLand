package fr.thejordan.historyland.object.common;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.helper.TT;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BItem {

    private final ItemStack stack;
    public ItemStack stack() { return stack; }

    private BItem(ItemStack stack){ this.stack = stack; }
    public static BItem of(ItemStack itemStack) {
        return new BItem(itemStack.clone());
    }
    public static BItem of(Material material) { return new BItem(new ItemStack(material)); }
    public static BItem of(ConfigurationSection section) {
        Material material = TT.Material.material(section.getString("material","GRASS_BLOCK"));
        BItem item = of(material)
                .displayName(section.getString("name","..."))
                .amount(section.getInt("amount",1))
                .damage(section.getInt("damage",0))
                .lore(section.getStringList("lore"));
        if (material == Material.PLAYER_HEAD && section.isString("skin"))
            item.skin(new Skin(section.getString("skin")));
        if (item.stack().getItemMeta() instanceof LeatherArmorMeta && section.isString("color"))
            if (Helper.isRGB(section.getString("color")))
                item.color(Helper.parseRGB(section.getString("color")));
        return item;
    }

    public BItem type(Material material) {
        stack().setType(material);
        return this;
    }
    public Material getType() {
        return stack().getType();
    }
    public BItem displayName(String displayName) {
        changeMeta((meta) -> meta.setDisplayName(displayName));
        return this;
    }
    public String displayName() {
        return (getItemMeta() != null && getItemMeta().hasDisplayName()) ? getItemMeta().getDisplayName() : "";
    }

    public BItem lore(String... lore) {
        changeMeta((meta) -> meta.setLore(List.of(lore)));
        return this;
    }
    public List<String> lore() {
        return (getItemMeta() != null && getItemMeta().hasLore()) ? getItemMeta().getLore() : List.of();
    }

    public BItem aLore(String... lore) {
        changeMeta((meta) -> {
            List<String> current = (meta.getLore() == null) ? new ArrayList<>() : new ArrayList<>(meta.getLore());
            current.addAll(List.of(lore));
            meta.setLore(current);
        });
        return this;
    }
    public BItem model(int id) {
        changeMeta((meta) -> meta.setCustomModelData(id));
        return this;
    }

    public BItem enchant(Enchantment enchantment, int level) {
        stack().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public BItem skin(Skin skin) {
        GameProfile profile = new GameProfile(Helper.uuidWithSeed(skin.getTexture()), null);
        profile.getProperties().put("textures", new Property("textures", skin.getTexture()));
        Field profileField;
        SkullMeta meta = (SkullMeta) getItemMeta();
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
            profileField = meta.getClass().getDeclaredField("serializedProfile");
            profileField.setAccessible(true);
            profileField.set(meta, new NBTTagCompound());
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        stack.setItemMeta(meta);
        return this;
    }

    public BItem lore(List<String> lore) {
        changeMeta((meta) -> meta.setLore(lore));
        return this;
    }

    public BItem amount(int amount) {
        stack().setAmount(amount);
        return this;
    }
    public int amount() {
        return stack().getAmount();
    }

    public BItem glow() {
        enchant(Enchantment.DURABILITY, 1);
        changeMeta((meta) -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
        return this;
    }

    public BItem prefix(String prefix) {
        String name = stack().getItemMeta().getDisplayName();
        changeMeta((meta) -> meta.setDisplayName(prefix + name));
        return this;
    }

    public BItem suffix(String suffix) {
        String name = stack().getItemMeta().getDisplayName();
        changeMeta((meta) -> meta.setDisplayName(name + suffix));
        return this;
    }

    public <T,Z> BItem sData(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
        changeMeta((meta) -> meta.getPersistentDataContainer().set(key, type, value));
        return this;
    }

    public <T,Z> Z gData(NamespacedKey key, PersistentDataType<T,Z> type) {
        ItemMeta meta = this.getItemMeta();
        return meta.getPersistentDataContainer().get(key, type);
    }

    public <T,Z> boolean hData(NamespacedKey key, PersistentDataType<T,Z> type) {
        ItemMeta meta = this.getItemMeta();
        return meta.getPersistentDataContainer().has(key, type);
    }

    public BItem flags(ItemFlag... flags) {
        changeMeta((meta) -> meta.addItemFlags(flags));
        return this;
    }

    public BItem unbreakable() {
        changeMeta((meta) -> meta.setUnbreakable(true));
        return this;
    }

    public BItem damage(int damage) {
        changeMeta((meta) -> {
            if (meta instanceof Damageable) ((Damageable) meta).setDamage(damage);
        });
        return this;
    }

    public ItemMeta getItemMeta() {
        if (stack().getItemMeta() == null) stack().setItemMeta(Bukkit.getItemFactory().getItemMeta(stack.getType()));
        return stack.getItemMeta();
    }
    public BItem changeMeta(Consumer<ItemMeta> metaFunction) {
        ItemMeta meta = getItemMeta();
        metaFunction.accept(meta);
        stack().setItemMeta(meta);
        return this;
    }

    public BItem color(Color color) {
        changeMeta((meta) -> {
            if (meta instanceof LeatherArmorMeta) ((LeatherArmorMeta) meta).setColor(color);
        });
        return this;
    }

    public Color color() {
        if (getItemMeta() instanceof LeatherArmorMeta) return ((LeatherArmorMeta) getItemMeta()).getColor();
        return null;
    }

}
