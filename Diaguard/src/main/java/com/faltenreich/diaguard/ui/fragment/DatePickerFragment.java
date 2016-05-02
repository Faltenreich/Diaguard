package com.faltenreich.diaguard.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import org.joda.time.DateTime;

/**
 * Created by Filip on 23.10.13.
 */
public class DatePickerFragment extends DialogFragment {

    private static final String DATE_PICKER_FRAGMENT_DATE = "DATE_PICKER_FRAGMENT_DATE";

    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(DateTime dateTime, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setOnDateSetListener(listener);
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DATE_PICKER_FRAGMENT_DATE, dateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime date = (DateTime) getArguments().getSerializable(DATE_PICKER_FRAGMENT_DATE);
        if (date == null) {
            date = new DateTime();
        }

        int year = date.getYear();
        // Months are zero-based due to the Calendar.class
        int month = date.getMonthOfYear() - 1;
        int day = date.getDayOfMonth();

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public void show(FragmentManager manager) {
        super.show(manager, DatePickerFragment.class.getSimpleName());
    }
}