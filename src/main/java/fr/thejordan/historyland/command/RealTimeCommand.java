package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.manager.RealTimeManager;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.Time;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class RealTimeCommand extends AbstractCommand {

    @Override
    public String id() { return "realtime"; }

    @Override
    public String description() { return null; }

    @Override
    public String usage() { return null; }

    @Override
    public String permission() { return "historyland.realtime"; }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                World world = Helper.getSendersLocation(commandSender).getWorld();
                if (RealTimeManager.instance().getRealTime().isOn(world)) {
                    RealTimeManager.instance().getRealTime().remove(world);
                    commandSender.sendMessage("§cRealTime stopé !");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                Time time = null;
                if (Helper.isInt(args[1])) time = new Time().setHour(Helper.toInt(args[1]));
                else if (Time.isValidTime(args[1])) time = Time.fromString(args[1]);
                if (time != null) {
                    World world = Helper.getSendersLocation(commandSender).getWorld();
                    if(world == null) return false;
                    world.setTime(time.toTicks());
                    RealTimeManager.instance().getRealTime().add(world, time.toTicks());
                    return sendMessageT(commandSender, "§aLe temps du monde " + world.getName() + " est mis à " + time.timeString());
                } else return sendMessageT(commandSender, "§c/realtime set <H/HM(S)>");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> returned = List.of();
        if (args.length == 1) returned = Arrays.asList("set", "stop");
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) returned = Arrays.asList("H","0H0M(0S)");
        return Helper.autocomplete(args[args.length - 1], returned);
    }

}
