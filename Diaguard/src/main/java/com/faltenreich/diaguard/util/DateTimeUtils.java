package com.faltenreich.diaguard.util;

import org.joda.time.DateTime;

/**
 * Created by Faltenreich on 23.03.2016.
 */
public class DateTimeUtils {

    public static String toWeekDayShort(DateTime dateTime, boolean withDot) {
        String weekDay = dateTime.toString("E");
        if (!withDot && weekDay.length() >= 1) {
            weekDay = weekDay.substring(0, weekDay.length() - 1);
        }
        return weekDay;
    }

    public static String toWeekDayShort(DateTime dateTime) {
        return toWeekDayShort(dateTime, false);
    }
}
