package com.faltenreich.diaguard.util;

import androidx.annotation.NonNull;

public class StringUtils {

    public static boolean isBlank(String text) {
        return text == null || text.trim().length() == 0;
    }

    @NonNull
    public static String newLine() {
        String system = System.getProperty("line.separator");
        return system != null ? system : "\n";
    }
}
