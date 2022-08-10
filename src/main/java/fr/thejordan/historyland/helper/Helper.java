package fr.thejordan.historyland.helper;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

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
}
