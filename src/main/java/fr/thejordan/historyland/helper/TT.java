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
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            String world = location.getWorld().getName();
            return String.format("%.2f;%.2f;%.2f;%.2f;%.2f;%s", x, y, z, yaw, pitch, world);
        }

        public static org.bukkit.Location location(String string) {
            String[] split = string.split(";");
            double x = Float.parseFloat(split[0].replace(",", "."));
            double y = Float.parseFloat(split[1].replace(",", "."));
            double z = Float.parseFloat(split[2].replace(",", "."));
            float yaw = 0F;
            float pitch = 0F;
            String world;
            if (split.length == 6) {
                yaw = Float.parseFloat(split[3].replace(",", "."));
                pitch = Float.parseFloat(split[4].replace(",", "."));
                world = split[5];
            } else world = split[3];
            World bukkitWorld = Bukkit.getWorld(world);
            if (bukkitWorld == null) bukkitWorld = Bukkit.getWorlds().get(0);
            return new org.bukkit.Location(bukkitWorld, x, y, z, yaw, pitch);
        }

    }
    public static class Skin {

        public static String string(fr.thejordan.historyland.object.common.Skin skin) {
            return skin.getTexture() + "|" + skin.getSignature();
        }

        public static fr.thejordan.historyland.object.common.Skin skin(String string) {
            if (string.contains("|")) {
                String[] split = string.split("\\|");
                return new fr.thejordan.historyland.object.common.Skin(split[0], split[1]);
            } else return new fr.thejordan.historyland.object.common.Skin(string);
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
