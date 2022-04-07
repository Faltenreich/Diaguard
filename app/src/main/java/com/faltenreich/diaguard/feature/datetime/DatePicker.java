package com.faltenreich.diaguard.feature.datetime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.joda.time.DateTime;

public class DatePicker {

    @NonNull private final DateTime date;
    @Nullable private final Consumer<DateTime> callback;

    private DatePicker(
        @NonNull DateTime date,
        @Nullable Consumer<DateTime> callback
    ) {
        this.date = date;
        this.callback = callback;
    }

    public void show(FragmentManager fragmentManager) {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(date.getMillis())
            .build();
        if (callback != null) {
            picker.addOnNegativeButtonClickListener(view -> callback.accept(null));
            picker.addOnPositiveButtonClickListener(selection -> callback.accept(new DateTime(selection)));
        }
        picker.show(fragmentManager, null);
    }

    public static class Builder {

        @Nullable private DateTime date;
        @Nullable private Consumer<DateTime> callback;

        public Builder() {

        }

        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        public Builder callback(Consumer<DateTime> callback) {
            this.callback = callback;
            return this;
        }

        public DatePicker build() {
            return new DatePicker(date != null ? date : DateTime.now(), callback);
        }
    }
}