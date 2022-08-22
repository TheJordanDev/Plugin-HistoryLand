package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.MotdManager;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.motd.Motd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class MotdCommand extends AbstractCommand {
    @Override
    public String id() {
        return "motd";
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
        return "historyland.motd";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            return sendMessageT(commandSender,"/motd <line(1/2)> <message>");
        } else if (args.length >= 2) {
            if (!List.of("line1","line2").contains(args[0].toLowerCase())) return false;
            boolean line = (args[0].equalsIgnoreCase("line1"));
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            message = (message + "&r").replace("\n", "");
            if (line) MotdManager.instance().getMotd().setLine1(message);
            else MotdManager.instance().getMotd().setLine2(message);
            commandSender.sendMessage("§aLigne " + (line ? 1 : 2) + " définie en :§r", Motd.formatLine(message));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return (args.length == 1) ? Helper.autocomplete(args[0], List.of("line1", "line2")) : List.of();
    }

}
