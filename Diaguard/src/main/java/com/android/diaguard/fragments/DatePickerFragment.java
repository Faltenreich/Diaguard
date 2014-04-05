package com.android.diaguard.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Filip on 23.10.13.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar today;

    public DatePickerFragment(Calendar presetTime) {
        this.today = presetTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(today == null)
            today = Calendar.getInstance();

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

    }
}