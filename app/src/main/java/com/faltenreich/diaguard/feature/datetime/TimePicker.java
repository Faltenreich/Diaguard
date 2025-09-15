package com.faltenreich.diaguard.feature.datetime;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentManager;

import com.faltenreich.diaguard.shared.Helper;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.DateTime;

public class TimePicker {

    @NonNull private final DateTime time;
    @TimeFormat private final int timeFormat;
    @Nullable private final Consumer<DateTime> callback;

    private TimePicker(
        @NonNull DateTime time,
        @TimeFormat int timeFormat,
        @Nullable Consumer<DateTime> callback
    ) {
        this.time = time;
        this.timeFormat = timeFormat;
        this.callback = callback;
    }

    public void show(FragmentManager fragmentManager) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
            .setTimeFormat(timeFormat)
            .setHour(time.getHourOfDay())
            .setMinute(time.getMinuteOfDay())
            // Workaround: com.google.android.material:material:1.7.+ changed the default inputMode to INPUT_MODE_KEYBOARD
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build();
        if (callback != null) {
            picker.addOnNegativeButtonClickListener(view -> callback.accept(null));
            picker.addOnPositiveButtonClickListener(selection -> callback.accept(time
                .withHourOfDay(picker.getHour())
                .withMinuteOfHour(picker.getMinute())));
        }
        picker.show(fragmentManager, null);
    }

    public static class Builder {

        @Nullable private DateTime time;
        @Nullable private Consumer<DateTime> callback;

        public Builder() {}

        public Builder time(DateTime time) {
            this.time = time;
            return this;
        }

        public Builder callback(Consumer<DateTime> callback) {
            this.callback = callback;
            return this;
        }

        public TimePicker build(Context context) {
            return new TimePicker(
                    time != null ? time : DateTime.now(),
                    Helper.is24HourFormat(context) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H,
                    callback
            );
        }
    }
}