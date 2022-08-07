package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.ShopManager;
import fr.thejordan.historyland.object.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand extends AbstractCommand {

    @Override
    public String id() { return "shop"; }
    @Override
    public String description() { return null; }
    @Override
    public String usage() { return "/shop <nom>"; }
    @Override
    public String permission() { return "historyland.shop"; }
    @Override
    public List<String> aliases() { return null; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return sendMessageT(sender,"Vous devez être un joueur pour utiliser cette commande.");
        if (args.length != 1) return false;
        String shopName = args[0];
        if (!ShopManager.instance().getShops().containsKey(shopName)) return sendMessageT(sender, "Le shop "+shopName+" n'éxiste pas !");
        ShopManager.instance().getShops().get(shopName).open(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned;
        if (args.length == 1) returned = Helper.autocomplete(args[0], new ArrayList<>(ShopManager.instance().getShops().keySet()));
        else returned = List.of();
        return returned;
    }
}
