package fr.thejordan.historyland.command;

import fr.thejordan.historyland.manager.SeatManager;
import fr.thejordan.historyland.object.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SitCommand extends AbstractCommand {

    @Override public String id() { return "sit"; }
    @Override
    public String description() {
        return null;
    }
    @Override
    public String usage() {
        return "/sit";
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
        if (!(sender instanceof Player player)) return sendMessageF(sender, "You must be a player to use this command.");
        SeatManager.instance().sit(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
