package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.MainManager;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class ChatCommand extends AbstractCommand {

    @Override
    public String id() {
        return "hchat";
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
        return "historyland.chat";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return sendMessageT(sender,"§c/hchat <on/off>");
        else if (args.length == 1) {
            if (!List.of("on","off","toggle").contains(args[0].toLowerCase())) return sendMessageT(sender,"§c/hchat <on/off>");
            boolean newState;
            if (args[0].equalsIgnoreCase("toggle")) newState = !MainManager.instance().isChat();
            else newState = (args[0].equalsIgnoreCase("on"));
            Translator.broadcast((newState) ? "chat_enabled" : "chat_disabled", Map.of(), List.of());
            MainManager.instance().setChat(newState);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = List.of();
        if (args.length == 1) returned = List.of("on","off","toggle");
        return Helper.autocomplete(args[args.length - 1], returned);
    }
}
