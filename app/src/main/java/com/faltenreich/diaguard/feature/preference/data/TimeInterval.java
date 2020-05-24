package com.faltenreich.diaguard.feature.preference.data;

import androidx.annotation.Nullable;

public enum  TimeInterval {

    CONSTANT(0, 24),
    EVERY_SIX_HOURS(4, 6),
    EVERY_FOUR_HOURS(0, 4),
    EVERY_SECOND_HOUR(0, 2),
    EVERY_HOUR(0, 1);

    public int startingHour;
    public int rangeInHours;

    TimeInterval(int startingHour, int rangeInHours) {
        this.rangeInHours = rangeInHours;
        this.startingHour = startingHour;
    }

    @Nullable
    public static TimeInterval ofRangeInHours(int rangeInHours) {
        for (TimeInterval timeInterval : TimeInterval.values()) {
            if (timeInterval.rangeInHours == rangeInHours) {
                return timeInterval;
            }
        }
        return null;
    }
}
