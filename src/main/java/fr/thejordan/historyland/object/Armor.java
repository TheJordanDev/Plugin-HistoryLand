package fr.thejordan.historyland.object;

import fr.thejordan.historyland.object.collectible.Collectible;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Armor {

    private final Player player;

    @Getter
    private ItemStack helmet;
    @Getter
    private ItemStack chestplate;
    @Getter
    private ItemStack leggings;
    @Getter
    private ItemStack boots;
    @Getter
    private ItemStack offhand;

    public Armor setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public Armor setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    public Armor setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public Armor setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public Armor setOffHand(ItemStack offhand) {
        this.offhand = offhand;
        return this;
    }

    public Armor setFromType(ItemStack itemStack, Collectible.ItemType type) {
        switch (type) {
            case HEAD -> setHelmet(itemStack);
            case CHESTPLATE -> setChestplate(itemStack);
            case LEGGINGS -> setLeggings(itemStack);
            case BOOTS -> setBoots(itemStack);
            case OTHER -> setOffHand(itemStack);
        }
        return this;
    }

    public ItemStack getFromType(Collectible.ItemType type) {
        switch (type) {
            case HEAD -> { return getHelmet(); }
            case CHESTPLATE -> { return getChestplate(); }
            case LEGGINGS -> { return getLeggings(); }
            case BOOTS -> { return getBoots(); }
            case OTHER -> { return getOffhand(); }
        }
        return null;
    }

    public Armor(Player player) {
        this.player = player;
        player.getInventory().getItemInOffHand();
        this.offhand = player.getInventory().getItemInOffHand();
        this.helmet = (player.getInventory().getHelmet() == null ? new ItemStack(Material.AIR) : player.getInventory().getHelmet());
        this.chestplate = (player.getInventory().getChestplate() == null ? new ItemStack(Material.AIR) : player.getInventory().getChestplate());
        this.leggings = (player.getInventory().getLeggings() == null ? new ItemStack(Material.AIR) : player.getInventory().getLeggings());
        this.boots = (player.getInventory().getBoots() == null ? new ItemStack(Material.AIR) : player.getInventory().getBoots());
    }

    public void apply() {
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItemInOffHand(offhand);
    }

    public boolean wearsItem(ItemStack itemStack) {
        return (helmet.isSimilar(itemStack)
            || chestplate.isSimilar(itemStack)
            || leggings.isSimilar(itemStack)
            || boots.isSimilar(itemStack)
            || offhand.isSimilar(itemStack));
    }

    @Override
    public String toString() {
        return "Armor{" +
                "player=" + player.getName() +
                ", helmet=" + ((helmet==null)?"null":helmet.getType().name()) +
                ", chestplate=" + ((chestplate==null)?"null":chestplate.getType().name()) +
                ", leggings=" + ((leggings==null)?"null":leggings.getType().name()) +
                ", boots=" + ((boots==null)?"null":boots.getType().name()) +
                ", offhand=" + ((offhand==null)?"null":offhand.getType().name()) +
                '}';
    }
}
