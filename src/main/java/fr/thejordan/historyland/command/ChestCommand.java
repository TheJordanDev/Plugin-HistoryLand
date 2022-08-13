package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChestCommand extends AbstractCommand {
    @Override
    public String id() { return "chest"; }

    @Override
    public String description() { return null; }

    @Override
    public String usage() { return null; }

    @Override
    public String permission() { return "historyland.chest"; }

    @Override
    public List<String> aliases() { return null; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof BlockCommandSender)) return false;
        if (args.length >= 1 && args.length < 4) {
            if (args[0].equalsIgnoreCase("open")) sender.sendMessage("§c/chest open [X] [Y] [Z]");
            else if (args[0].equalsIgnoreCase("close")) sender.sendMessage("§c/chest close [X] [Y] [Z]");
        } else if (args.length == 4) {
            if (!List.of("open","close").contains(args[0].toLowerCase())) return false;
            if (Helper.isInt(args[1]) && Helper.isInt(args[2]) && Helper.isInt(args[3])) {
                boolean open = args[0].equalsIgnoreCase("open");
                World world = Helper.getWorldFromSender(sender);
                Location chestLocation = new Location(world, Helper.toInt(args[1]), Helper.toInt(args[2]), Helper.toInt(args[3]));
                if (Helper.isChest(chestLocation.getBlock())) {
                    Helper.toggleChest(chestLocation.getBlock(), open);
                    sender.sendMessage("§aCoffre " + ((open) ? "Ouvert" : "Fermé"));
                } else sender.sendMessage("§cLe bloc doit-être un coffre/ender chest !");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> returned = new ArrayList<>();
        if (args.length == 1) returned = List.of("open","close");
        else if (args.length >= 2 && args.length <= 4) {
            Block target = null;
            if (commandSender instanceof Player player) target = player.getTargetBlock(null, 5);
            else if (commandSender instanceof BlockCommandSender bCS) target = bCS.getBlock();
            if (target != null) {
                if (Helper.isChest(target)) returned = Collections.singletonList(((args.length == 2) ? "X" : (args.length == 3) ? "Y" : "Z"));
                else returned = Collections.singletonList(""+ ((args.length == 2) ? target.getX() : (args.length == 3) ? target.getY() : target.getZ()));
            }
        }
        return returned;
    }

}
