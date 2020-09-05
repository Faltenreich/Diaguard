package com.faltenreich.diaguard.shared;

import android.content.Context;
import android.graphics.Color;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class Helper {

    private static final String FORMAT_TIME = "HH:mm";

    public static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    @Deprecated
    public static Locale getLocale() {
        return getLocale(DiaguardApplication.getContext());
    }

    public static String getLanguageCode() {
        return getLocale().getLanguage();
    }

    public static String getCountryCode() {
        return getLocale().getCountry();
    }

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.mediumDate();
    }

    public static DateTimeFormatter getTimeFormat() {
        return DateTimeFormat.forPattern(FORMAT_TIME);
    }

    public static String getTextAgo(Context context, int differenceInMinutes) {
        if (differenceInMinutes < 2) {
            return context.getString(R.string.latest_moments);
        }

        String textAgo = context.getString(R.string.latest);

        if (differenceInMinutes > 2879) {
            differenceInMinutes = differenceInMinutes / 60 / 24;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.days));
        } else if (differenceInMinutes > 119) {
            differenceInMinutes = differenceInMinutes / 60;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.hours));
        } else {
            textAgo = textAgo.replace("[unit]", context.getString(R.string.minutes));
        }

        return textAgo.replace("[value]", Integer.toString(differenceInMinutes));
    }

    public static int colorBrighten(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb((int) (r * percent), (int) (g * percent), (int) (b * percent));
    }

    public static <T extends Enum<?>> T valueOf(Class<T> enumeration, String search) {
        if (enumeration != null && enumeration.getEnumConstants() != null) {
            for (T each : enumeration.getEnumConstants()) {
                if (each.name().compareToIgnoreCase(search) == 0) {
                    return each;
                }
            }
        }
        return null;
    }

    public static float calculateHbA1c(float avgMgDl) {
        return 0.031f * avgMgDl + 2.393f;
    }

    public static float parseKcalToKj(float kcal) {
        return kcal * 4.184f;
    }
}
