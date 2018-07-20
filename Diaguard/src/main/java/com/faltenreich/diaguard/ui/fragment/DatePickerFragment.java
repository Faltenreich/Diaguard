package com.faltenreich.diaguard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import org.joda.time.DateTime;

public class DatePickerFragment extends DialogFragment {

    private static final String DATE_PICKER_FRAGMENT_DATE_SELECTED = "DATE_PICKER_FRAGMENT_DATE_SELECTED";
    private static final String DATE_PICKER_FRAGMENT_DATE_MIN = "DATE_PICKER_FRAGMENT_DATE_MIN";
    private static final String DATE_PICKER_FRAGMENT_DATE_MAX = "DATE_PICKER_FRAGMENT_DATE_MAX";

    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(@Nullable DateTime selectedDateTime, @Nullable DateTime minDateTime, @Nullable DateTime maxDateTime, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setOnDateSetListener(listener);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_SELECTED, selectedDateTime);
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_MIN, minDateTime);
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE_MAX, maxDateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static DatePickerFragment newInstance(DateTime selectedDateTime, DatePickerDialog.OnDateSetListener listener) {
        return newInstance(selectedDateTime, null, null, listener);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime selectedDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_SELECTED) : null;
        selectedDateTime = selectedDateTime != null ? selectedDateTime : DateTime.now();
        DateTime minDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_MIN) : null;
        DateTime maxDateTime = getArguments() != null ? (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE_MAX) : null;
        maxDateTime = maxDateTime != null ? maxDateTime : DateTime.now();

        int year = selectedDateTime.getYear();
        // Months are zero-based due to the Calendar.class
        int month = selectedDateTime.getMonthOfYear() - 1;
        int day = selectedDateTime.getDayOfMonth();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, day);
        if (minDateTime != null) {
            dialog.getDatePicker().setMinDate(minDateTime.getMillis());
        }
        dialog.getDatePicker().setMaxDate(maxDateTime.getMillis());

        return dialog;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void show(FragmentManager manager) {
        super.show(manager, DatePickerFragment.class.getSimpleName());
    }
}