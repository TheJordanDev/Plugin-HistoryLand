package fr.thejordan.historyland.command;

import fr.thejordan.historyland.object.AbstractCommand;
import fr.thejordan.historyland.object.collectible.CollectibleGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CollectiblesCommand extends AbstractCommand {
    @Override
    public String id() {
        return "collectibles";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return "/collectibles";
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
            return sendMessageF(sender,"You must be a player to use this command.");
        new CollectibleGUI(player).open();
        return true;
    }
}
