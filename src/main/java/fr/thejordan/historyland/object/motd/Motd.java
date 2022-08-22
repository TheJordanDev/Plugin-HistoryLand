package fr.thejordan.historyland.object.motd;

import fr.thejordan.historyland.helper.Helper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Motd {

    @Getter @Setter private String line1 = "";
    @Getter @Setter private String line2 = "";

    public Motd() {
        ArrayList<String> def = new ArrayList<>(List.of(Bukkit.getMotd().split("\n")));
        this.line1 = (def.size() >= 1) ? def.get(0) : "";
        this.line2 = (def.size() == 2) ? def.get(1) : "";
    }

    public Motd(String line1, String line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public String format() {
        StringJoiner returned = new StringJoiner("\n");
        returned.add((line1 == null || line1.isEmpty()) ? "§r" : formatLine(line1));
        returned.add((line2 == null || line2.isEmpty()) ? "§r" : formatLine(line2));
        return returned.toString();
    }

    public static String formatLine(String line) {
        String returned = line;
        returned = returned.replace("&", "§");
        for (int i = 1; i <= 9; i++)
            returned = returned.replaceAll("/s" + i, Helper.repeat(i, " "));
        return returned;
    }

}
