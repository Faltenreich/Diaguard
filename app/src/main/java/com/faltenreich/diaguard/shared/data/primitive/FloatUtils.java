package com.faltenreich.diaguard.shared.data.primitive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.feature.preference.data.PreferenceCache;

import java.text.NumberFormat;
import java.util.Locale;

public class FloatUtils {

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }

    public static String parseFloat(float number) {
        int decimalPlaces = PreferenceCache.getInstance().getDecimalPlaces();
        return parseFloat(number, decimalPlaces);
    }

    public static String parseFloat(float number, int scale) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.ROOT);
        format.setMaximumFractionDigits(scale);
        return format.format(number);
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
