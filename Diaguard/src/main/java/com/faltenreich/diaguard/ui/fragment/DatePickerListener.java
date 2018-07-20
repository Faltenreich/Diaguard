package com.faltenreich.diaguard.ui.fragment;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

public interface DatePickerListener {
    void onDatePicked(@Nullable DateTime dateTime);
}
