package fr.thejordan.historyland.object.common;

import fr.thejordan.historyland.helper.Helper;
import lombok.Getter;

public class Position {

    @Getter
    private final Number number;
    @Getter
    private final boolean relative;

    private Position(boolean isRelative, Number number) {
        this.number = number;
        this.relative = isRelative;
    }

    public static Position parse(String arg) {
        Number number = 0;
        boolean isRelative = (arg.startsWith("~"));
        if (isRelative) {
            String _temp = arg.replaceFirst("~", "");
            if (!_temp.equals("") && Helper.isNumber(_temp)) number = Helper.toNumber(_temp);
        } else {
            if (!Helper.isNumber(arg)) return null;
            number = Helper.toNumber(arg);
        }
        return new Position(isRelative, number);
    }

}
