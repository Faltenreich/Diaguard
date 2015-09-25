package com.faltenreich.diaguard.util;

/**
 * Created by Faltenreich on 20.09.2015.
 */
public class StringUtils {

    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

}
