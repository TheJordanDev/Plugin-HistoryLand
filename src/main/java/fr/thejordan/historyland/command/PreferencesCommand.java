package fr.thejordan.historyland.command;

import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.preferences.PreferenceGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PreferencesCommand extends AbstractCommand {

    @Override
    public String id() { return "preferences"; }
    @Override
    public String description() { return null; }
    @Override
    public String usage() { return null; }
    @Override
    public String permission() { return null; }
    @Override
    public List<String> aliases() {
        return List.of("prefs");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        new PreferenceGUI(player).open();
        return super.onCommand(sender, command, label, args);
    }
}
