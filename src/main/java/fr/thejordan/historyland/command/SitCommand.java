package fr.thejordan.historyland.command;

import fr.thejordan.historyland.object.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        //TODO: SIT
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
