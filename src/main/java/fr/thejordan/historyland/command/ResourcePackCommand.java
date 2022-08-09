package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.ResourcePackManager;
import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourcePackCommand extends AbstractCommand {

    @Override
    public String id() {
        return "resourcepack";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return "/resourcepack";
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
        if (!(sender instanceof Player player))
            return sendMessageF(sender, "You must be a player to use this command.");
        if (args.length == 0 || !player.hasPermission("historyland.resourcepack.bypass"))
            return ResourcePackManager.instance().sendPack(player, false);
        if (args.length == 1 && args[0].equalsIgnoreCase("bypass")) {
            UUID uuid = player.getUniqueId();
            if (ResourcePackManager.instance().getBypassList().contains(uuid)) {
                ResourcePackManager.instance().getBypassList().remove(uuid);
                return sendMessageT(player, Translator.translate(player,"resourcepack_bypass_off"));
            } else {
                ResourcePackManager.instance().getBypassList().add(uuid);
                return sendMessageT(player, Translator.translate(player,"resourcepack_bypass_on"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = new ArrayList<>();
        if (!(sender instanceof Player player)) return returned;
        if (args.length == 1 && player.hasPermission("historyland.resourcepack.bypass"))
            returned.addAll(Helper.autocomplete(args[0], List.of("bypass")));
        return returned;
    }
}
