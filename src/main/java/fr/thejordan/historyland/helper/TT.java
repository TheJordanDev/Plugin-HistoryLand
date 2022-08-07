package fr.thejordan.historyland.helper;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Optional;

public class TT {

    public static class Location {

        public static String string(org.bukkit.Location location) {
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            String world = location.getWorld().getName();
            return String.format("%f|%f|%f|%s", x, y, z, world);
        }

        public static org.bukkit.Location location(String string) {
            String[] split = string.split(";");
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            String world = split[3];
            World bukkitWorld = Bukkit.getWorld(world);
            if (bukkitWorld == null) bukkitWorld = Bukkit.getWorlds().get(0);
            return new org.bukkit.Location(bukkitWorld, x, y, z);
        }

    }
    public static class Skin {

        public static String string(fr.thejordan.historyland.object.Skin skin) {
            return skin.getTexture() + "|" + skin.getSignature();
        }

        public static fr.thejordan.historyland.object.Skin skin(String string) {
            if (string.contains("|")) {
                String[] split = string.split("\\|");
                return new fr.thejordan.historyland.object.Skin(split[0], split[1]);
            } else return new fr.thejordan.historyland.object.Skin(string);
        }

    }
    public static class UUID {

            public static String string(java.util.UUID uuid) {
                return uuid.toString().replace("-", "");
            }

            private static java.util.UUID uuidNoHyph(String string) {
                String withHyphens = string.replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
                Optional<java.util.UUID> uuid = Helper.getUUID(withHyphens);
                return uuid.orElse(null);
            }



            public static java.util.UUID uuid(String string) {
                if (!string.contains("-")) return uuidNoHyph(string);
                Optional<java.util.UUID> uuid = Helper.getUUID(string);
                return uuid.orElse(null);
            }

    }
    public static class Material {

            public static String string(org.bukkit.Material material) {
                return material.name();
            }

            public static org.bukkit.Material material(String string) {
                Optional<org.bukkit.Material> value = Helper.isValueOfEnum(org.bukkit.Material.class, string);
                return value.orElse(org.bukkit.Material.GRASS_BLOCK);
            }

    }

}
