package fr.thejordan.historyland.object.light;

import fr.thejordan.historyland.helper.Helper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.List;



public class LightFunctions {

    public static List<String> lightLevels = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");

    public static String setLightBlock(CommandSender sender, String[] args) {
        try {
            LightParams params = LightParams.parse(sender, LightParams.LightType.SET,args);
            World world = Helper.getWorldFromSender(sender);
            if (world == null) return "§cVous n'êtes pas dans un monde.";
            Location lightLocation = new Location(world,params.x,params.y,params.z);
            Block block = lightLocation.getBlock();
            Material type = block.getType();
            if (type.isSolid())
                return "Le bloc ne doit pas être solide !";
            Helper.setLight(lightLocation,params.level,params.waterlogged);
            return "§aCommande executée avec succés";
        } catch (CommandParsingError e) {
            return e.getMessage();
        }
    }


    public static String fadeLightBlock(CommandSender sender, String[] args) {
        try {
            LightParams params = LightParams.parse(sender, LightParams.LightType.FADE, args);
            World world = Helper.getWorldFromSender(sender);
            if (world == null) return "§cVous n'êtes pas dans un monde.";
            Location lightLocation = new Location(world,params.x,params.y,params.z);
            Material type = lightLocation.getBlock().getType();
            if (type.isSolid())
                return "Le bloc ne doit pas être solide !";
            Helper.setLight(lightLocation,params.level,params.waterlogged);
            LightFadeScheduler.fade(lightLocation, params.waterlogged, params.level, params.targetLevel, params.ticks);
            return "§aCommande executée avec succés";
        } catch (CommandParsingError e) {
            return e.getMessage();
        }
    }

    public static class LightParams {

        @Getter @Setter public double x, y, z;
        @Getter @Setter public int level;
        @Getter @Setter public int targetLevel;
        @Getter @Setter public boolean waterlogged = false;
        @Getter @Setter public int ticks = 0;

        public static LightParams parse(CommandSender sender, LightType type, String[] args) throws CommandParsingError {
            LightParams params = new LightParams();
            List<String> cmdArgs = List.of(args);
            params.setX(getInt(getArg(cmdArgs,0),"X doit-être un entier !"));
            params.setY(getInt(getArg(cmdArgs,1),"Y doit-être un entier !"));
            params.setZ(getInt(getArg(cmdArgs,2),"Z doit-être un entier !"));
            if (type == LightType.SET) {
                int lightLevel = getInt(getArg(cmdArgs,3),"Le niveau de lumière doit-être un entier !");
                if (lightLevel >= 0 && lightLevel <= 15) params.setLevel(lightLevel);
                else throw new CommandParsingError("Le niveau de lumère doit-être entre 0 et 15 !");
                if (args.length == 5)
                    params.setWaterlogged(getBoolean(getArg(cmdArgs,4),"Waterlogged doit être un booléen !"));
            } else if (type == LightType.FADE) {
                int startLevel = getInt(getArg(cmdArgs,3),"Le niveau de lumière doit-être un entier !");
                if (startLevel >= 0 && startLevel <= 15) params.setLevel(startLevel);
                else throw new CommandParsingError("Le niveau de lumère doit-être entre 0 et 15 !");

                int endLevel = getInt(getArg(cmdArgs,4),"Le niveau de lumière doit-être un entier !");
                if (endLevel >= 0 && endLevel <= 15) params.setTargetLevel(endLevel);
                else throw new CommandParsingError("Le niveau de lumère doit-être entre 0 et 15 !");

                int ticks = getInt(getArg(cmdArgs,5),"Le temps doit-être un entier !");
                if (ticks < 0) ticks = ticks*-1;
                params.setTicks(ticks);
                if (args.length == 7)
                    params.setWaterlogged(getBoolean(getArg(cmdArgs,6),"Waterlogged doit être un booléen !"));
            }
            return params;
        }

        public static String getArg(List<String> array, int index) {
            if (index >= 0 && index < array.size()) return array.get(index);
            return null;
        }

        public static int getInt(String arg, String ifNotError) throws CommandParsingError {
            if (arg == null) throw new CommandParsingError("Un argument est manquant.");
            else if (!Helper.isInt(arg)) throw new CommandParsingError(ifNotError);
            return Integer.parseInt(arg);
        }

        public static boolean getBoolean(String arg, String ifNotError) throws CommandParsingError {
            if (arg == null) throw new CommandParsingError("Un argument est manquant.");
            else if (!Helper.isBoolean(arg)) throw new CommandParsingError(ifNotError);
            return Boolean.parseBoolean(arg);
        }

        public enum LightType {
            SET, FADE
        }
    }
    public static class CommandParsingError extends Exception {

        public CommandParsingError(String message) {
            super(message);
        }
    }


}
