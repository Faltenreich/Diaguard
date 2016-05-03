package com.faltenreich.diaguard.util;

/**
 * Created by Faltenreich on 08.04.2016.
 */
public class NumberUtils {

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }

    public static int setFlag(int flags, int mask, boolean value) {
        if (value) {
            flags |= mask;
        } else {
            flags &= ~mask;
        }
        return flags;
    }

    public static boolean getFlag(int flags, int mask) {
        return (flags & mask) == mask;
    }
}
