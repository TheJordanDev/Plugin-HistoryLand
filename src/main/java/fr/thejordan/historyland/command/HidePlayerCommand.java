package fr.thejordan.historyland.command;

import fr.thejordan.historyland.Historyland;
import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.Translator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HidePlayerCommand extends AbstractCommand {
    @Override
    public String id() {
        return "visibility";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return sendMessageT(sender,"§cThis command can only be used in-game.");
        if (args.length == 0) return sendMessageT(sender, "§c/visibility <on/off>");
        else if (args.length == 1) {
            if (!List.of("on","off").contains(args[0].toLowerCase())) return sendMessageT(sender,"§c/visibility <on/off>");
            boolean newState = (args[0].equalsIgnoreCase("on"));
            Translator.send(player, (newState) ? "visibility_enabled" : "visibility_disabled");
            for (Player target : Bukkit.getOnlinePlayers().stream().filter(p -> !p.getUniqueId().equals(player.getUniqueId())).toList()) {
                if (newState) player.showPlayer(Historyland.instance(), target);
                else player.hidePlayer(Historyland.instance(), target);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = List.of();
        if (args.length == 1) returned = List.of("on","off");
        return Helper.autocomplete(args[args.length - 1], returned);
    }
}

