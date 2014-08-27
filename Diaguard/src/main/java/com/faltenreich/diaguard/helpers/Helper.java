package com.faltenreich.diaguard.helpers;

import android.content.Context;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * Created by Filip on 10.12.13.
 */
public class Helper {
    public static final String DB_FORMAT_DATE_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DB_FORMAT_TIME = "HH:mm";
    public static final String PLACEHOLDER = "-";

    public static DecimalFormat getDecimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat;
    }

    public static DecimalFormat getRationalFormat() {
        return new DecimalFormat("#");
    }

    private static String getTimePattern() {
        return DB_FORMAT_TIME;
    }

    private static String getDatePattern() {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(DiaguardApplication.getContext().getApplicationContext());
        return ((SimpleDateFormat)dateFormat).toLocalizedPattern();
    }

    public static DateTimeFormatter getDateDatabaseFormat() {
        return DateTimeFormat.forPattern(DB_FORMAT_DATE_DB);
    }

    public static DateTimeFormatter getDateFormat() {
        return DateTimeFormat.forPattern(getDatePattern());
    }

    public static DateTimeFormatter getTimeFormat() {
        return DateTimeFormat.forPattern(getTimePattern());
    }

    public static float formatCalendarToHourMinutes(DateTime time) {
        int hour = time.getHourOfDay();
        int minute = time.getMinuteOfHour();
        return hour + ((float)minute / 60.0f);
    }

    public static float getDPI(Context context, float pixels) {
        return pixels * (context.getResources().getDisplayMetrics().densityDpi / 160);
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

    public static String toStringDelimited(String[] array, char delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : array) {
            stringBuilder.append(string);
            stringBuilder.append(delimiter);
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
