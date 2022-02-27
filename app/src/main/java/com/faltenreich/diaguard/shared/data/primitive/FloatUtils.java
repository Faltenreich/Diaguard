package com.faltenreich.diaguard.shared.data.primitive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FloatUtils {

    private static final int DECIMAL_FRACTION_DIGITS = 2;

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }

    // TODO: Make scale dynamic via SharedPreferences
    public static String parseFloat(float number, int scale) {
        DecimalFormat format = new DecimalFormat("0", DecimalFormatSymbols.getInstance());
        format.setMaximumFractionDigits(scale);
        return format.format(number);
    }

    public static String parseFloat(float number) {
        return parseFloat(number, DECIMAL_FRACTION_DIGITS);
    }

    public static float parseNumber(@NonNull String number) {
        try {
            return Float.parseFloat(number.trim().replaceAll(",", "."));
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public static Float parseNullableNumber(@Nullable String number) {
        try {
            return number != null
                ? Float.parseFloat(number.trim().replaceAll(",", "."))
                : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
