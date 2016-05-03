package com.faltenreich.diaguard.util;

/**
 * Created by Faltenreich on 08.04.2016.
 */
public class NumberUtils {

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }
}
