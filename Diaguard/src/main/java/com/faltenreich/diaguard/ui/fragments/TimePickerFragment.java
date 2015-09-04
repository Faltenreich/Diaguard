package com.faltenreich.diaguard.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.joda.time.DateTime;

/**
 * Created by Filip on 23.10.13.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TIME = "Time";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime time = (DateTime)getArguments().getSerializable(TIME);

        if(time == null)
            time = new DateTime();

        int hourOfDay = time.getHourOfDay();
        int minute = time.getMinuteOfHour();

        return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    }
}