package com.faltenreich.diaguard.feature.datetime;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.joda.time.DateTime;

public class DateRangePicker {

    @Nullable private final DateTime startDate;
    @Nullable private final DateTime endDate;
    @Nullable private final Consumer<Pair<DateTime, DateTime>> callback;

    private DateRangePicker(
        @Nullable DateTime startDate,
        @Nullable DateTime endDate,
        @Nullable Consumer<Pair<DateTime, DateTime>> callback
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.callback = callback;
    }

    public void show(FragmentManager fragmentManager) {
        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setSelection(new Pair<>(startDate != null ? startDate.getMillis() : 0, endDate != null ? endDate.getMillis() : 0))
            .build();
        if (callback != null) {
            picker.addOnNegativeButtonClickListener(view -> callback.accept(null));
            picker.addOnPositiveButtonClickListener(selection -> callback.accept(new Pair<>(new DateTime(selection.first), new DateTime(selection.second))));
        }
        picker.show(fragmentManager, null);
    }

    public static class Builder {

        @Nullable private DateTime startDate;
        @Nullable private DateTime endDate;
        @Nullable private Consumer<Pair<DateTime, DateTime>> callback;

        public Builder() {

        }

        public Builder startDate(DateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(DateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder callback(Consumer<Pair<DateTime, DateTime>> callback) {
            this.callback = callback;
            return this;
        }

        public DateRangePicker build() {
            return new DateRangePicker(startDate, endDate, callback);
        }
    }
}