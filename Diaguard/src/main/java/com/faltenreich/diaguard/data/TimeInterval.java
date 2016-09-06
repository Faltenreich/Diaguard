package com.faltenreich.diaguard.data;

/**
 * Created by Faltenreich on 04.09.2016.
 */
public enum  TimeInterval {

    CONSTANT(24, 0),
    EVERY_SIX_HOURS(6, 4),
    EVERY_FOUR_HOURS(4, 0),
    EVERY_SECOND_HOUR(2, 0),
    EVERY_HOUR(1, 0);

    public int interval;
    public int startHour;

    TimeInterval(int interval, int startHour) {
        this.interval = interval;
        this.startHour = startHour;
    }
}
