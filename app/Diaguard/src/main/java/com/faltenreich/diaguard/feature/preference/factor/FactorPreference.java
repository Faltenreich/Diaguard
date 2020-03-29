package com.faltenreich.diaguard.feature.preference.factor;

import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import butterknife.BindView;

/**
 * Created by Filip on 04.11.13.
 */
abstract class FactorPreference extends DialogPreference {

    @BindView(R.id.preference_time_spinner) Spinner spinner;
    @BindView(R.id.preference_time_list) RecyclerView list;

    private FactorListAdapter adapter;
    private TimeInterval timeInterval;

    FactorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_value_list);
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.spinner = (Spinner) view.findViewById(R.id.preference_time_spinner);
        this.list = (RecyclerView) view.findViewById(R.id.preference_time_list);
        this.timeInterval = getTimeInterval();
        init();
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        // Workaround for showing keyboard on focusing EditText in RecyclerView
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            store();
        }
    }

    private void init() {
        initSpinner();
        initList();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_rhythm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (timeInterval.ordinal() < spinner.getCount()) {
            spinner.setSelection(timeInterval.ordinal());
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                timeInterval = TimeInterval.values()[position];
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        adapter = new FactorListAdapter(getContext());
        list.setAdapter(adapter);
        updateList();
    }

    private void updateList() {
        adapter.clear();
        DateTime dateTime = DateTime.now().withHourOfDay(timeInterval.startHour);
        while (adapter.getItemCount() < DateTimeConstants.HOURS_PER_DAY / timeInterval.interval) {
            int hourOfDay = dateTime.getHourOfDay();
            adapter.addItem(new FactorListItem(timeInterval, hourOfDay, getValueForHour(hourOfDay)));
            dateTime = dateTime.withHourOfDay((hourOfDay + timeInterval.interval) % DateTimeConstants.HOURS_PER_DAY);
        }
        adapter.notifyDataSetChanged();
    }

    private void store() {
        setTimeInterval(timeInterval);

        for (int pos = 0; pos < adapter.getItemCount(); pos++) {
            FactorListItem preference = adapter.getItem(pos);
            int hoursIntoInterval = 0;

            while (hoursIntoInterval < preference.getInterval().interval) {
                int hourOfDay = (preference.getHourOfDay() + hoursIntoInterval) % DateTimeConstants.HOURS_PER_DAY;
                storeValueForHour(preference.getValue(), hourOfDay);
                hoursIntoInterval++;
            }
        }

        onPreferenceUpdate();
    }

    protected void onPreferenceUpdate() {

    }

    protected abstract void setTimeInterval(TimeInterval interval);

    protected abstract TimeInterval getTimeInterval();

    protected abstract void storeValueForHour(float value, int hourOfDay);

    protected abstract float getValueForHour(int hourOfDay);
}