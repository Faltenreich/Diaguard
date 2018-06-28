package com.faltenreich.diaguard.util;

public class NumberUtils {

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }

    public static float parseNumber(String number) {
        try {
            return Float.parseFloat(number.trim().replaceAll(",", "."));
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static Float parseNullableNumber(String number) {
        try {
            return number != null ? Float.parseFloat(number.trim().replaceAll(",", ".")) : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
