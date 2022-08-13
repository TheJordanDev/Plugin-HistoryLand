package fr.thejordan.historyland.command;

import fr.thejordan.historyland.helper.Helper;
import fr.thejordan.historyland.object.common.AbstractCommand;
import fr.thejordan.historyland.object.common.Position;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TprCommand extends AbstractCommand {
    @Override
    public String id() {
        return "tpr";
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
        return "historyland.tpr";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 9) return sendHelper(commandSender);
        else {
            if (!Helper.isPosition(args[0])) return sendMessageT(commandSender, "§cX doit être une position !");
            Position X = Position.parse(args[0]);
            if (!Helper.isPosition(args[1])) return sendMessageT(commandSender, "§cY doit être une position !");
            Position Y = Position.parse(args[1]);
            if (!Helper.isPosition(args[2])) return sendMessageT(commandSender, "§cZ doit être une position !");
            Position Z = Position.parse(args[2]);
            if (!Helper.isEntity(args[3])) return sendMessageT(commandSender, "§centity doit être un type d'entité !");
            EntityType type = Helper.toEntity(args[3]);
            String name = (args[4].equalsIgnoreCase("null")) ? null : args[4];
            if (!Helper.isPosition(args[5])) return sendMessageT(commandSender, "§cdX doit être un double !");
            Position dX = Position.parse(args[5]);
            if (!Helper.isPosition(args[6])) return sendMessageT(commandSender, "§cdY doit être un double !");
            Position dY = Position.parse(args[6]);
            if (!Helper.isPosition(args[7])) return sendMessageT(commandSender, "§cdZ doit être un double !");
            Position dZ = Position.parse(args[7]);
            if (!Helper.isDouble(args[8])) return sendMessageT(commandSender, "§cr doit être un double !");
            double r = Helper.toDouble(args[8]);
            Location origin = Helper.getRelativeFromSender(commandSender, dX, dY, dZ);
            Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin, r, r, r, (e) -> {
                if (name != null)
                    return e.getType().equals(type) && (e.getCustomName() != null && e.getCustomName().equalsIgnoreCase(name));
                else return e.getType().equals(type);
            });
            entities.forEach((e) -> {
                Location target = Helper.getRelativeFromEntity(origin, e, X, Y, Z);
                ((CraftEntity) e).getHandle().setLocation(target.getX(), target.getY(), target.getZ(), e.getLocation().getYaw(), e.getLocation().getPitch());
            });
            //entities.forEach((e)->e.teleport(target));
            commandSender.sendMessage("§aToutes les entités ont été téléportés !");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returned = new ArrayList<>();
        if (args.length >= 1 && args.length <= 3) {
            Block target = null;
            if (sender instanceof Player player) target = player.getTargetBlock(null, 5);
            else if (sender instanceof BlockCommandSender bCS) target = bCS.getBlock();
            List<String> complete = new ArrayList<>();
            if (target == null) complete.add("~");
            else complete.add(""+((args.length == 1) ? target.getX() : (args.length == 2) ? target.getY() : target.getZ()));
            returned.addAll(Helper.autocomplete(args[args.length-1], complete));
        } else if (args.length == 4)
            returned = Helper.autocomplete(args[3], Stream.of(EntityType.values()).map(EntityType::name).collect(Collectors.toList()));
        else if (args.length == 5) returned = Helper.autocomplete(args[4], List.of("null"));
        else if (args.length >= 6 && args.length <= 8) {
            Block target = null;
            if (sender instanceof Player player) target = player.getTargetBlock(null, 5);
            else if (sender instanceof BlockCommandSender bCS) target = bCS.getBlock();
            List<String> complete = new ArrayList<>();
            if (target == null) complete.add("~");
            else complete.add(""+((args.length == 6) ? target.getX() : (args.length == 7) ? target.getY() : target.getZ()));
            returned.addAll(Helper.autocomplete(args[args.length-1], complete));
        } else if (args.length == 9) returned = Helper.autocomplete(args[8], List.of("rayon"));
        return returned;
    }

    public boolean sendHelper(CommandSender sender) {
        sender.sendMessage(
                "§6*----------§8[§dTPR§8]§6----------*",
                " ",
                "§6Voici les differentes manières d'utiliser cette commande:",
                " ",
                " §c/tpr [X] [Y] [Z] [entity] [name] [dX] [dY] [dZ] [r]",
                " ",
                " §4§o[X] [Y] [Z]: position voulue.",
                " §2§o[name]: Nom de l'entité (null si aucun nom)",
                " §3§o[entity]: Type de l'entité.",
                " §5§o[dX] [dY] [dZ]: Distance X,Y,Z voulue",
                " §d§o[r]: Rayon de detection autour de la coordonnée",
                " ",
                "§6*-------------------------*"
        );
        return false;
    }

}
