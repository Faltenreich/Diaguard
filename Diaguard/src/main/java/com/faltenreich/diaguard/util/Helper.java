package com.faltenreich.diaguard.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DimenRes;
import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.Random;

/**
 * Created by Filip on 10.12.13.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Helper {

    private static final String TAG = Helper.class.getSimpleName();
    private static final String FORMAT_TIME = "HH:mm";

    public static Locale getLocale() {
        return DiaguardApplication.getContext().getResources().getConfiguration().locale;
    }

    public static boolean isSystemLocale(String languageCode) {
        return languageCode != null && isSystemLocale(new Locale(languageCode));
    }

    public static boolean isSystemLocale(Locale locale) {
        Locale systemLocale = getLocale();
        String systemLanguage = systemLocale != null ? systemLocale.getLanguage() : null;
        String language = locale != null ? locale.getLanguage() : null;
        return systemLanguage != null && language != null && systemLanguage.equals(language);
    }

    public static String getLanguageCode() {
        return getLocale().getLanguage();
    }

    public static String getCountryCode() {
        return getLocale().getCountry();
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

        return ( (float) ( (int) (
                value >= 0
                        ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
                        : (tmpSub >= -0.5f ? tmp : tmp - 1)
        ) ) ) / pow;
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
        return String.format(getLocale(), "%.1f", number);
    }

    private static String parseFloatWithoutDigit(float number) {
        return String.format(getLocale(), "%d", (int) number);
    }

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.mediumDate();
    }

    public static DateTimeFormatter getTimeFormat() {
        return DateTimeFormat.forPattern(FORMAT_TIME);
    }

    public static float getDPI(@DimenRes int dimenResId) {
        return DiaguardApplication.getContext().getResources().getDimensionPixelSize(dimenResId);
    }

    public static String getTextAgo(Context context, int differenceInMinutes) {
        if(differenceInMinutes < 2) {
            return context.getString(R.string.latest_moments);
        }

        String textAgo = context.getString(R.string.latest);

        if(differenceInMinutes > 2879) {
            differenceInMinutes = differenceInMinutes / 60 / 24;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.days));
        }
        else if(differenceInMinutes > 119) {
            differenceInMinutes = differenceInMinutes / 60;
            textAgo = textAgo.replace("[unit]", context.getString(R.string.hours));
        }
        else {
            textAgo = textAgo.replace("[unit]", context.getString(R.string.minutes));
        }

        return  textAgo.replace("[value]", Integer.toString(differenceInMinutes));
    }

    public static int colorBrighten(int color, float percent) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb((int) (r * percent), (int) (g * percent), (int) (b * percent));
    }

    public static String toStringDelimited(String[] array, char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : array) {
            stringBuilder.append(string);
            stringBuilder.append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static <T extends Enum<?>> T valueOf(Class<T> enumeration, String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

    public static  <T extends Measurement> void createTestData() {
        final int entryCount = 500;
        Random random = new Random();
        for (int i = 0; i < entryCount; i++) {
            Entry entry = new Entry();
            entry.setDate(DateTime.now().minusHours(entryCount - i));
            EntryDao.getInstance().createOrUpdate(entry);
            int categoryIndex = random.nextInt(Measurement.Category.values().length - 1);
            Measurement.Category category = Measurement.Category.values()[categoryIndex];
            try {
                T measurement = (T) category.toClass().newInstance();
                measurement.setValues(new float[]{111});
                measurement.setEntry(entry);
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
            } catch (java.lang.InstantiationException e) {

            } catch (IllegalAccessException e) {

            }
            Log.d(TAG, String.format("Added %d/%d", i, entryCount));
        }
    }

    public static float calculateHbA1c(float avgMgDl) {
        return 0.031f * avgMgDl + 2.393f;
    }

    public static float parseKcalToKj(float kcal) {
        return kcal * 4.184f;
    }
}
