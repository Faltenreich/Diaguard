package com.android.diaguard.helpers;

import android.app.Activity;

import com.android.diaguard.database.Event;

/**
 * Created by Filip on 05.11.13.
 */
public class Validator {

    public static boolean validateEventValue(Activity activity, Event.Category category, float value) {

        int resourceIdExtrema = activity.getResources().getIdentifier(category.name().toLowerCase() +
                "_extrema", "array", activity.getPackageName());
        int[] extrema = activity.getResources().getIntArray(resourceIdExtrema);

        return value > extrema[0] && value < extrema[1];
    }

    public static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }
}
