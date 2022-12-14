package fr.thejordan.historyland.object.shop;

import com.earth2me.essentials.User;
import fr.thejordan.historyland.manager.CollectibleManager;
import fr.thejordan.historyland.manager.MainManager;
import fr.thejordan.historyland.object.collectible.Collectible;
import fr.thejordan.historyland.object.common.BItem;
import fr.thejordan.historyland.object.common.Keys;
import fr.thejordan.historyland.object.common.Translator;
import lombok.Getter;
import net.ess3.api.MaxMoneyException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.Map;

public class ShopItem extends Collectible {

    @Getter
    private final Double price;

    public ShopItem(Collectible item, Integer amount, Double price) {
        super(item.getId(), item.getName(), item.getMaterial(), amount, item.getLore(), item.getModelID(), item.getDamage(), item.getType(),item.getData());
        this.price = price;
    }

    public ItemStack shopDisplay(Player player) {
        boolean owns = CollectibleManager.ownes(player,this);
        BItem item = BItem.of(toItemStack())
                .displayName((owns?"§a§l☑ ":"§6§l☐ ")+getName())
                .aLore("§6" + price+" "+MainManager.instance().data().getCurrency())
                .sData(Keys.SHOP_ITEM_KEY, PersistentDataType.STRING, "true");
        return item.stack();
    }

    public static ShopItem load(ConfigurationSection section) {
        String id = "item";
        double price = 0D;
        int amount = 1;
        for (String key : section.getKeys(false)) {
            switch (key) {
                case "id" -> id = section.getString(key,id);
                case "price"-> price = section.getDouble(key, 0D);
                case "amount" -> amount = section.getInt(key,1);
            }
        }
        Collectible loaded = CollectibleManager.getByID(id);
        if (loaded == null) return new ShopItem(Collectible.unknownItem,amount,Double.MAX_VALUE);
        else return new ShopItem(loaded, amount, price);
    }

    public void buy(Player player) {
        User user = MainManager.instance().essentials().getUser(player);
        if (CollectibleManager.ownes(player,this)) {
            Translator.send(player,"shop_already_bought", Map.of("item",getName()));
            return;
        }
        if (user.getMoney().doubleValue() >= price) {
            try {
                user.setMoney(BigDecimal.valueOf(user.getMoney().doubleValue()-price));
                Translator.send(player,"shop_purchase_successful",
                        Map.of("amount",""+getAmount(),"item",getName()));
                CollectibleManager.add(player.getUniqueId(),toCollectible());
            } catch (MaxMoneyException e) {
                Translator.send(player,"shop_purchase_error");
            }
        } else {
            Translator.send(player,"shop_purchase_not_enough_found");
        }
    }

    public Collectible toCollectible() {
        return CollectibleManager.getByID(getId());
    }



}