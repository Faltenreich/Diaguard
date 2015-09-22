package com.faltenreich.diaguard.util;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class StringUtils {

    public static boolean isBlank(String string) {
        return string != null && string.trim().length() > 0;
    }

}
