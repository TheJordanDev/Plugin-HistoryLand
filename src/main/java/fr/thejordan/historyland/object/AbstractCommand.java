package fr.thejordan.historyland.object;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public abstract String id();

    public abstract String description();
    public abstract String usage();
    public abstract String permission();
    public String permissionMessage() {
        return "§cVous n'avez pas la permission d'utiliser cette commande.";
    }
    public abstract List<String> aliases();

    public void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(id());
        if (description() != null && !description().isBlank()) command.setDescription(description());
        if (permission() != null && !permission().isBlank()) command.setPermission(permission());
        if (permissionMessage() != null && !permissionMessage().isBlank()) command.setPermissionMessage(permissionMessage());
        if (aliases() != null && !aliases().isEmpty()) command.setAliases(aliases());
        if (usage() != null && !usage().isBlank()) command.setUsage(usage());
        command.setExecutor(this);
        command.setTabCompleter(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }

    public boolean sendMessageT(CommandSender sender, String message) {
        sender.sendMessage(message);
        return true;
    }

    public boolean sendMessageF(CommandSender sender, String message) {
        sender.sendMessage(message);
        return true;
    }

}
