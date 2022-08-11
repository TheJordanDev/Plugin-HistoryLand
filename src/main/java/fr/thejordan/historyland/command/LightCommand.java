package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.light.LightFunctions;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LightCommand extends AbstractCommand {

    @Override
    public String id() {
        return "light";
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
        return "historyland.light";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return sendMessageT(sender, "");
        else {
            if (args[0].equalsIgnoreCase("set"))
                return sendMessageT(sender, LightFunctions.setLightBlock(sender, Arrays.copyOfRange(args,1,args.length)));
            else if (args[0].equalsIgnoreCase("fade"))
                return sendMessageT(sender, LightFunctions.fadeLightBlock(sender,Arrays.copyOfRange(args,1,args.length)));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = new ArrayList<>();
        if (args.length == 1) returned = Helper.autocomplete(args[0], List.of("set","fade"));
        else if (args.length > 1) {
            if (!List.of("set","fade").contains(args[0].toLowerCase())) return returned;
            boolean isSet = args[0].equalsIgnoreCase("set");
            String lastArg = args[args.length-1];
            if (args.length <= 4) {
                if (sender instanceof Player player) {
                    Block lookingAt = player.getTargetBlock((Set<Material>)null,5);
                    returned.addAll(Helper.autocomplete(lastArg,List.of(""+
                            ((args.length == 2) ? lookingAt.getX() : (args.length == 3) ? lookingAt.getY() : lookingAt.getZ()))));
                } else if (sender instanceof BlockCommandSender bc) {
                    Block block = bc.getBlock();
                    returned.addAll(Helper.autocomplete(lastArg,List.of(""+
                            ((args.length == 2) ? block.getX() : (args.length == 3) ? block.getY() : block.getZ()))));
                }
                return returned;
            } else if (args.length == 5) returned = Helper.autocomplete(lastArg, LightFunctions.lightLevels);
            else if (args.length == 6) {
                if (isSet) returned = Helper.autocomplete(lastArg, List.of("true","false"));
                else returned = Helper.autocomplete(lastArg, LightFunctions.lightLevels);
            } else if (!isSet) {
                if (args.length == 7) returned = Helper.autocomplete(lastArg,List.of("<ticks>"));
                else if (args.length == 8) returned = Helper.autocomplete(lastArg,List.of("true","false"));
            }
        }
        return returned;
    }
}
