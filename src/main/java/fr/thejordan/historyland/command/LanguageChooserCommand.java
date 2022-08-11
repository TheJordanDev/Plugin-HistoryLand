package fr.thejordan.historyland.command;

import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.language.LanguageChooserGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LanguageChooserCommand extends AbstractCommand {

    @Override
    public String id() {
        return "language";
    }
    @Override
    public String description() {
        return "Change your language";
    }
    @Override
    public String usage() {
        return "/language";
    }
    @Override
    public String permission() {
        return null;
    }
    @Override
    public List<String> aliases() {
        return List.of("lang");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return sendMessageF(sender, "&cVous devez être un joueur pour utiliser cette commande");
        new LanguageChooserGUI(player).open();
        return true;
    }
}
