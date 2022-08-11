package fr.thejordan.historyland.helper;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Light;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Helper {

    public static Pattern bracketPattern = Pattern.compile("(\\[|\\])");
    public static Pattern vectorPattern = Pattern.compile("\\[-?[0-9]*\\.[0-9]*+;-?[0-9]*\\.[0-9]*+;-?[0-9]*\\.[0-9]*\\]+");
    public static Pattern locationPattern = Pattern.compile("-?[0-9]*\\.[0-9]*+;-?[0-9]*\\.[0-9]*+;-?[0-9]*\\.[0-9]*;.[\\s\\S]*");

    public static Optional<UUID> getUUID(String str) {
        return Optional.of(UUID.fromString(str));
    }

    public static List<String> autocomplete(String written, List<String> words) {
        List<String> returned = new ArrayList<>();
        if (written.isBlank()) return words;
        else {
            for (String word : words) {
                if (word.toUpperCase().startsWith(written.toUpperCase())) returned.add(word);
            }
        }
        return returned;
    }

    public static <T> Optional<T> isValueOfEnum(Class<T> enumClass, String value) {
        for (T o : enumClass.getEnumConstants()) {
            if (o.toString().equals(value)) return Optional.of(o);
        }
        return Optional.empty();
    }

    @SafeVarargs
    public static <T> List<T> addToList(List<T> list, T... element) {
        list.addAll(List.of(element));
        return list;
    }

    //COLOR

    public static Matcher getRGBMatch(String test) {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        return c.matcher(test);
    }

    public static boolean isRGB(String t) {
        return getRGBMatch(t).matches();
    }

    public static Color parseRGB(String input) {
        Matcher m = getRGBMatch(input);
        if (m.matches()) {
            return Color.fromRGB(Integer.parseInt(m.group(1)),  // r
                    Integer.parseInt(m.group(2)),  // g
                    Integer.parseInt(m.group(3))); // b
        }
        return null;
    }

    public static Material woolFromColor(Color color) {
        return woolFromColor(color.getRed(),color.getGreen(),color.getBlue());
    }

    public static Material woolFromColor(int red, int green, int blue) {
        int distance = Integer.MAX_VALUE;
        DyeColor closest = null;
        for (DyeColor dye : DyeColor.values()) {
            Color color = dye.getColor();
            int dist = Math.abs(color.getRed() - red) + Math.abs(color.getGreen() - green) + Math.abs(color.getBlue() - blue);
            if (dist < distance) {
                distance = dist;
                closest = dye;
            }
        }
        return Material.getMaterial((closest.name() + "_wool").toUpperCase());
    }

    public static boolean isInt(String page) {
        try {
            Integer.parseInt(page);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static UUID uuidWithSeed(String seed) {
        return UUID.nameUUIDFromBytes(seed.getBytes());
    }

    public static ItemStack removeAllEnchants(ItemStack itemStack) {
        ItemStack s = itemStack.clone();
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            s.removeEnchantment(enchantment);
        }
        return s;
    }

    public static Optional<String> isStringURL(String _url) {
        try {
            new java.net.URL(_url);
            return Optional.of(_url);
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    public static float getYawComplex(BlockFace b) {
        if (b == BlockFace.NORTH) return 180F;
        else if (b == BlockFace.EAST) return -90F;
        else if (b == BlockFace.SOUTH) return 0F;
        else if (b == BlockFace.WEST) return 90F;
        else return 0F;
    }

    public static Vector getStairDirection(Stairs stair) {
        Stairs.Shape shape = stair.getShape();
        BlockFace face = stair.getFacing().getOppositeFace();
        if (shape == Stairs.Shape.STRAIGHT) return face.getDirection().multiply(0.3);
        else return new Vector(0, 0, 0);
    }

    public static float getYawFromStair(Stairs stair) {
        Stairs.Shape shape = stair.getShape();
        BlockFace face = stair.getFacing().getOppositeFace();
        if (shape == Stairs.Shape.STRAIGHT) return getYawComplex(face);
        float offset;
        if (shape.name().endsWith("_LEFT")) offset = -45F;
        else offset = 45F;
        return getYawComplex(face)+ offset;
    }

    public static BukkitRunnable runnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() { runnable.run(); }
        };
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBoolean(String s) {
        try {
            Boolean.parseBoolean(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static World getWorldFromSender(CommandSender sender) {
        if (sender instanceof Player player) return player.getWorld();
        else if (sender instanceof BlockCommandSender bc) return bc.getBlock().getWorld();
        else return Bukkit.getWorlds().get(0);
    }

    public static void setLight(Location location, int level, boolean waterlogged) {
        Block block = location.getBlock();
        if (level == 0) { block.setType((waterlogged) ? Material.WATER :Material.AIR); return; }
        if (block.getType() != Material.LIGHT) {
            block.setType(Material.LIGHT);
            Light light = (Light)block.getBlockData();
            light.setLevel(0);
            light.setWaterlogged(waterlogged);
            block.setBlockData(light);
        }
        Light light = (Light)block.getBlockData();
        light.setWaterlogged(waterlogged);
        light.setLevel(level);
        block.setBlockData(light);
    }

    public static boolean isRelative(String text) {
        if (text.startsWith("~")) {
            String coord = text.replaceFirst("~","");
            return Helper.isDouble(coord);
        }
        return false;
    }

    public static double getRelative(String text) {
        if (isRelative(text)) {
            String coord = text.replaceFirst("~","");
            return Double.parseDouble(coord);
        }
        return 0;
    }

    public static double parseCoord(CommandSender sender,String coord) {
        if (Helper.isDouble(coord)) return Double.parseDouble(coord);
        if (sender instanceof Player) {
            if (Helper.isRelative(coord)) return getRelative(coord);
        } else if (sender instanceof BlockCommandSender){
            if (Helper.isRelative(coord)) return getRelative(coord);
        }
        return 0;
    }

    public static void hoverMessage(ComponentBuilder builder, List<String> lines) {
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, lines.stream().map((Text::new)).collect(Collectors.toList()));
        builder.event(event);
    }

    public static void command(ComponentBuilder builder, String command) {
        ClickEvent event = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        builder.event(event);
    }

    public static void addHoverMessage(ComponentBuilder builder, String origin, List<String> lines) {
        ComponentBuilder nB = new ComponentBuilder(origin);
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, lines.stream().map((Text::new)).collect(Collectors.toList()));
        nB.event(event);
        builder.append(nB.create());
    }

    public static void addCommandMessage(ComponentBuilder builder, String origin, String command) {
        ComponentBuilder nB = new ComponentBuilder(origin);
        ClickEvent event = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        nB.event(event);
        builder.append(nB.create());
    }

    public static void addHoverCommandMessage(ComponentBuilder builder, String origin, String command, List<String> lines) {
        ComponentBuilder nB = new ComponentBuilder(origin);
        ClickEvent cmd = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, lines.stream().map((Text::new)).collect(Collectors.toList()));
        nB.event(cmd);
        nB.event(hover);
        builder.append(nB.create());
    }

    public static <T> ArrayList<T[]> chunks(ArrayList<T> bigList,int n){
        ArrayList<T[]> chunks = new ArrayList<T[]>();
        for (int i = 0; i < bigList.size(); i += n) {
            T[] chunk = (T[])bigList.subList(i, Math.min(bigList.size(), i + n)).toArray();
            chunks.add(chunk);
        }
        return chunks;
    }

    public static String locationToReadableString(Location location) {
        return " - X: "+location.getX()+"\n"
                + " - Y: "+location.getY()+"\n"
                + " - Z: "+location.getZ()+"\n"
                +" - W: "+location.getWorld().getName();
    }

    public static boolean isStringVector(String input) {
        return input.replaceAll(",",".").matches(vectorPattern.pattern());
    }

    public static boolean isStringLocation(String input) {
        return input.replaceAll(",",".").matches(locationPattern.pattern());
    }

    public static Vector vectorFromString(String input) {
        if (!isStringVector(input)) return new Vector(0,0,0);
        String[] nums = input.replaceAll(bracketPattern.pattern(),"").replaceAll(",",".").split(";");
        Number x = Float.parseFloat(nums[0]);
        Number y = Float.parseFloat(nums[1]);
        Number z = Float.parseFloat(nums[2]);
        return new Vector(x.doubleValue(),y.doubleValue(),z.doubleValue());
    }

    public static Location locationFromString(String input, World world) {
        if (!isStringLocation(input)) return null;
        String[] nums = input.replaceAll(",",".").split(";");
        Number x = Float.parseFloat(nums[0]);
        Number y = Float.parseFloat(nums[1]);
        Number z = Float.parseFloat(nums[2]);
        return new Location(world, x.doubleValue(),y.doubleValue(),z.doubleValue());
    }

    public static Location getSendersLocation(CommandSender sender) {
        Location origin;
        if (sender instanceof Player) origin = ((Player) sender).getLocation().clone();
        else if (sender instanceof BlockCommandSender)
            origin = ((BlockCommandSender) sender).getBlock().getLocation().clone();
        else origin = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        return origin;
    }

}
