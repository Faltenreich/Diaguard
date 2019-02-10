package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.format.DateFormat;

import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;

public class TimePickerFragment extends DialogFragment {
    private static final String TIME_PICKER_FRAGMENT_DATE = "TIME_PICKER_FRAGMENT_DATE";

    private TimePickerDialog.OnTimeSetListener listener;

    public static TimePickerFragment newInstance(DateTime dateTime, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setOnTimeSetListener(listener);
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(TIME_PICKER_FRAGMENT_DATE, dateTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        DateTime time = arguments != null ? (DateTime) getArguments().getSerializable(TIME_PICKER_FRAGMENT_DATE) : null;
        if (time == null) {
            time = new DateTime();
        }

        int hourOfDay = time.getHourOfDay();
        int minute = time.getMinuteOfHour();

        return new TimePickerDialog(getActivity(), R.style.DateTimePicker, listener, hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void show(FragmentManager manager) {
        super.show(manager, TimePickerFragment.class.getSimpleName());
    }
}