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
                if (Helper.isInt(args[1])) {
                    Time time = new Time().setHour(Helper.toInt(args[1]));
                    World world = Helper.getSendersLocation(commandSender).getWorld();
                    if(world == null) return false;
                    world.setTime(time.toTicks());
                    RealTimeManager.instance().getRealTime().add(world, time.toTicks());
                    commandSender.sendMessage("§aLe temps du monde " + world.getName() + " est mis à " + time.timeString());
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return (args.length == 1) ? Helper.autocomplete(args[0], Arrays.asList("set", "stop")) : List.of();
    }

}
