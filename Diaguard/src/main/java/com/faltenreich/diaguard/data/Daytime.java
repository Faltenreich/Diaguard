package com.faltenreich.diaguard.data;

import android.support.annotation.StringRes;

import com.faltenreich.diaguard.R;

/**
 * Created by Faltenreich on 06.09.2016.
 */
public enum  Daytime {

    MORNING(4, R.string.morning),
    NOON(10, R.string.noon),
    EVENING(16, R.string.evening),
    NIGHT(22, R.string.night);

    public static final int INTERVAL_LENGTH = 6;

    public int startingHour;
    public @StringRes int textResourceId;

    Daytime(int startingHour, @StringRes int textResourceId) {
        this.startingHour = startingHour;
        this.textResourceId = textResourceId;
    }

    public static Daytime toDayTime(int hourOfDay) {
        if (hourOfDay >=4 && hourOfDay < 10) {
            return MORNING;
        } else if (hourOfDay >= 10 && hourOfDay < 16) {
            return NOON;
        } else if (hourOfDay >= 16 && hourOfDay < 22) {
            return EVENING;
        } else {
            return NIGHT;
        }
    }
}
