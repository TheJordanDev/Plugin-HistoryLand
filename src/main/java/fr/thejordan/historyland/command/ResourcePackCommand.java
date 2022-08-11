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
import java.util.Optional;
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
        if (args.length == 0 || (!player.hasPermission("historyland.resourcepack.bypass") && !player.hasPermission("historyland.resourcepack.join")))
            return sendMessageF(sender, "You must specify a resourcepack.");
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("bypass")) {
                UUID uuid = player.getUniqueId();
                if (!player.hasPermission("historyland.resourcepack.bypass")) return ResourcePackManager.instance().sendPack(player, false);
                if (ResourcePackManager.instance().getBypassList().contains(uuid)) {
                    ResourcePackManager.instance().getBypassList().remove(uuid);
                    return sendMessageT(player, Translator.translate(player,"resourcepack_bypass_off"));
                } else {
                    ResourcePackManager.instance().getBypassList().add(uuid);
                    return sendMessageT(player, Translator.translate(player,"resourcepack_bypass_on"));
                }
            } else if (args[0].equalsIgnoreCase("change")) {
                if (!player.hasPermission("historyland.resourcepack.change")) return ResourcePackManager.instance().sendPack(player, false);
                return sendMessageT(player, "§6/resourcepack change <url>");
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("change")) {
                if (!player.hasPermission("historyland.resourcepack.change")) return ResourcePackManager.instance().sendPack(player, false);
                Optional<String> newUrl = Helper.isStringURL(args[1]);
                if (newUrl.isEmpty()) return sendMessageT(player, "§cInvalid URL.");
                sendMessageT(player, "§aNouveau resourcepack défini:","§e" + newUrl.get()," ");
                ResourcePackManager.instance().setPack(newUrl.get());
                return true;
            }
        }
        return ResourcePackManager.instance().sendPack(player, false);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = new ArrayList<>();
        if (!(sender instanceof Player player)) return returned;
        if (args.length == 1) {
            List<String> autocompleted = new ArrayList<>();
            if (player.hasPermission("historyland.resourcepack.bypass")) autocompleted.add("bypass");
            if (player.hasPermission("historyland.resourcepack.change")) autocompleted.add("change");
            returned.addAll(Helper.autocomplete(args[0],autocompleted));
        }
        return returned;
    }
}
