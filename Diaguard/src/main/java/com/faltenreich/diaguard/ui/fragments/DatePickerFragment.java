package com.faltenreich.diaguard.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.joda.time.DateTime;

/**
 * Created by Filip on 23.10.13.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String DATE = "Date";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime date = (DateTime)getArguments().getSerializable(DATE);

        if(date == null)
            date = new DateTime();

        int year = date.getYear();
        // Months are zero-based due to the Calendar.class
        int month = date.getMonthOfYear()-1;
        int day = date.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }
}