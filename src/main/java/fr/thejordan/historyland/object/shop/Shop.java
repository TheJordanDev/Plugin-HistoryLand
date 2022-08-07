package fr.thejordan.historyland.object.shop;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Shop {

    @Getter
    private final String name;
    @Getter
    private final ArrayList<ShopItem> items;

    public Shop(String name, ArrayList<ShopItem> items) {
        this.name = name;
        this.items = items;
    }

    public void open(Player player) {
        new ShopGUI(player, this).open();
    }

}