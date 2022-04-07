package com.faltenreich.diaguard.feature.datetime;

import androidx.fragment.app.FragmentManager;

import org.joda.time.DateTime;

public interface DatePicking {

    void goToDay(DateTime day);

    default void showDatePicker(DateTime day, FragmentManager fragmentManager) {
        new DatePicker.Builder()
            .date(day)
            .callback( dateTime -> {
                if (dateTime != null) {
                    goToDay(dateTime);
                }
            })
            .build()
            .show(fragmentManager);
    }
}
