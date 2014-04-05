package com.android.diaguard.helpers;

import android.content.Context;
import android.os.Environment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Filip on 10.12.13.
 */
public class Helper {

    public static final String PATH_STORAGE = Environment.getExternalStorageDirectory() + "/Diaguard";
    public static final String MIME_PDF = "application/pdf";
    public static final String WHITESPACE = " ";
    public static final String COMMA = ",";
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

    public static SimpleDateFormat getDateDatabaseFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static float formatCalendarToHourMinutes(Calendar time) {
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);
        return hour + ((float)minute / 60.0f);
    }

    public static float getDPI(Context context, float pixels) {
        return pixels * (context.getResources().getDisplayMetrics().densityDpi / 160);
    }

    public static int getDifferenceInDays(Calendar dateStart, Calendar dateEnd) {
        long diffMilliseconds = dateEnd.getTimeInMillis() - dateStart.getTimeInMillis();
        return (int)(diffMilliseconds / (24 * 60 * 60 * 1000));
    }

    public static int getDifferenceInHours(Calendar dateStart, Calendar dateEnd) {
        long diffMilliseconds = dateEnd.getTimeInMillis() - dateStart.getTimeInMillis();
        return (int)(diffMilliseconds / (60 * 60 * 1000));
    }

    public static int getDifferenceInMinutes(Calendar dateStart, Calendar dateEnd) {
        long diffMilliseconds = dateEnd.getTimeInMillis() - dateStart.getTimeInMillis();
        return (int)(diffMilliseconds / (60 * 1000));
    }
}
