package fr.thejordan.historyland.object.jet;

import fr.thejordan.historyland.object.common.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class JetUtils {

    public static FallingBlock throwBlock(Location origin, Material material, Vector vector) {
        FallingBlock fallingBlock = origin.getWorld().spawnFallingBlock(origin, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(vector);
        fallingBlock.setDropItem(false);
        fallingBlock.getPersistentDataContainer().set(Keys.JET_KEY, PersistentDataType.STRING, "true");
        return fallingBlock;
    }

    public static boolean isStringVector(String str) {
        String _s = str.replaceAll(",",".").replaceAll("\\[","").replaceAll("]","");
        String[] nums = _s.split(";");
        if (nums.length != 3) return false;
        return isNum(nums[0]) && isNum(nums[1]) && isNum(nums[2]);
    }

    public static Vector getVectorFromString(String str) {
        String _s = str.replaceAll("\\[","").replaceAll("]","");
        String[] nums = _s.split(";");
        if (nums.length != 3) return null;
        if (!isNum(nums[0]) || !isNum(nums[1]) || !isNum(nums[2])) return null;
        return new Vector(getNum(nums[0]),getNum(nums[1]),getNum(nums[2]));
    }

    public static Double getNum(String str) {
        try {
            String _s = str.replaceAll(",",".");
            return Double.parseDouble(_s);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNum(String str) {
        try {
            String _s = str.replaceAll(",",".");
            Double.parseDouble(_s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Entity> getNearbyEntities(Location location, double x, double y, double z, List<EntityType> types) {
        ArmorStand stand = location.getWorld().spawn(location,ArmorStand.class,(as)->{
            as.setVisible(false); as.setGravity(false); as.setInvulnerable(true);
        });
        List<Entity> e = stand.getNearbyEntities(x,y,z).stream().filter((entity -> types.contains(entity.getType()))).collect(Collectors.toList());
        stand.remove();
        return e;
    }

    public static Material strToMat(String name, Material def) {
        try {
            Material material = Material.getMaterial(name.toUpperCase());
            if (material == null) return def;
            return material;
        } catch (Exception e) {
            return def;
        }
    }

}
