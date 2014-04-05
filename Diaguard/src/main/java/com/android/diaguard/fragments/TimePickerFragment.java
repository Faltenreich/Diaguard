package com.android.diaguard.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Filip on 23.10.13.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Calendar now;

    public TimePickerFragment(Calendar presetTime) {
        this.now = presetTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(now == null)
            now = Calendar.getInstance();

        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
        return dialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    }
}