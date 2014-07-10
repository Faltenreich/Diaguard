package com.faltenreich.diaguard.helpers;

import android.content.Context;

import com.faltenreich.diaguard.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Filip on 10.12.13.
 */
public class Helper {

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
}
