package com.faltenreich.diaguard.feature.datetime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.DateTime;

public class TimePicker {

    @NonNull private final DateTime time;
    @Nullable private final Consumer<DateTime> callback;

    private TimePicker(
        @NonNull DateTime time,
        @Nullable Consumer<DateTime> callback
    ) {
        this.time = time;
        this.callback = callback;
    }

    public void show(FragmentManager fragmentManager) {
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
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

        public Builder() {

        }

        public Builder time(DateTime time) {
            this.time = time;
            return this;
        }

        public Builder callback(Consumer<DateTime> callback) {
            this.callback = callback;
            return this;
        }

        public TimePicker build() {
            return new TimePicker(time != null ? time : DateTime.now(), callback);
        }
    }
}