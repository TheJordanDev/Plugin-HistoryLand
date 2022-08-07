package fr.thejordan.historyland.command;

import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.*;
import fr.thejordan.historyland.object.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {

    @Override
    public String id() { return "hreload"; }
    @Override
    public String description() { return null; }
    @Override
    public String usage() { return null; }
    @Override
    public String permission() {
        return "historyland.reload";
    }
    @Override
    public List<String> aliases() {
        return List.of("hrl");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("all")) Historyland.instance().onReload();
            else if (args[0].equalsIgnoreCase("lang")) LanguageManager.instance().onReload();
            else if (args[0].equalsIgnoreCase("shop")) ShopManager.instance().onReload();
            else if (args[0].equalsIgnoreCase("collectible")) { CollectibleManager.instance().onReload(); ShopManager.instance().onReload(); }
            else if (args[0].equalsIgnoreCase("config")) MainManager.instance().onReload();
            else if (args[0].equalsIgnoreCase("warps")) WarpManager.instance().onReload();
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return Helper.autocomplete(args[0], List.of("all", "lang", "shop", "collectible","config","warps"));
        return List.of();
    }

}
