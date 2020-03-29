package com.faltenreich.diaguard.shared.data.primitive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.Helper;

import java.text.DecimalFormat;

public class FloatUtils {

    public static boolean isValid(Float number) {
        return !number.isNaN() && !number.isInfinite();
    }

    // https://stackoverflow.com/questions/8911356/whats-the-best-practice-to-round-a-float-to-2-decimals/45772416#45772416
    @SuppressWarnings("SameParameterValue")
    private static float round(float value, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        float tmp = value * pow;
        float tmpSub = tmp - (int) tmp;

        return ((float) ((int) (
            value >= 0
                ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                : (tmpSub >= -0.5f ? tmp : tmp - 1)
        ))) / pow;
    }

    public static String getDecimalSeparator() {
        return String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
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
            return number != null ? Float.parseFloat(number.trim().replaceAll(",", ".")) : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static String parseFloat(float number) {
        float rounded = round(number, 1);
        float digit = rounded % 1;
        boolean showDigit = digit > .0 || digit < -.0;
        return showDigit ?
            parseFloatWithDigit(rounded) :
            parseFloatWithoutDigit(rounded);
    }

    private static String parseFloatWithDigit(float number) {
        return String.format(Helper.getLocale(), "%.1f", number);
    }

    private static String parseFloatWithoutDigit(float number) {
        return String.format(Helper.getLocale(), "%d", (int) number);
    }
}
