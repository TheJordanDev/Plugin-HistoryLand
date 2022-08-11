package fr.thejordan.historyland.command;

import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.warps.WarpListGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand extends AbstractCommand {
    @Override
    public String id() {
        return "hwarps";
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return "/hwarps";
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
        new WarpListGUI(player).open();
        return true;
    }
}
