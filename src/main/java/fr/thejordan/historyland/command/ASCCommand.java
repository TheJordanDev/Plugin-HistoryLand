package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ASCCommand extends AbstractCommand {

    @Override
    public String id() { return "asc"; }

    @Override
    public String description() { return null; }

    @Override
    public String usage() { return null; }

    @Override
    public String permission() { return "historyland.asc"; }

    @Override
    public List<String> aliases() { return null; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player player)) return false;
        if (args.length >= 1) {
            if (!List.of("player","server").contains(args[0].toLowerCase())) return sendMessageT(commandSender, "§cUsage: /asc <player|server> <command...>");
            boolean isServer = args[0].equalsIgnoreCase("server");
            ItemStack wand = Items.ascmdWand(String.join(" ", Arrays.copyOfRange(args, 1, args.length)), isServer);
            player.getInventory().setItemInOffHand(wand);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> returned = new ArrayList<>();
        if (args.length == 1) returned =  Helper.autocomplete(args[0], Arrays.asList("player","server"));
        else if (args.length == 2) {
            HashMap<String, List<Command>> commands = Helper.getCommands();
            List<String> commandNames = new ArrayList<>();
            commands.values().forEach((list -> list.forEach(cmd->commandNames.add(cmd.getName()))));
            returned = Helper.autocomplete(args[1],commandNames);
        } else if (args.length > 2) {
            PluginCommand cmd = Bukkit.getPluginCommand(args[1]);
            if (cmd == null) return returned;
            returned = cmd.tabComplete(commandSender,args[1],Arrays.copyOfRange(args,2,args.length));
        }
        return returned;
    }


}
