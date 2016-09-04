package com.faltenreich.diaguard.ui.view.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.adapter.TimeAdapter;
import com.faltenreich.diaguard.adapter.list.ListItemTimePreference;
import com.faltenreich.diaguard.data.TimeInterval;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import butterknife.BindView;

/**
 * Created by Filip on 04.11.13.
 */
public class TimePreference extends DialogPreference {

    @BindView(R.id.preference_time_spinner)
    protected Spinner spinner;

    @BindView(R.id.preference_time_list)
    protected RecyclerView list;

    private Context context;
    private SharedPreferences sharedPreferences;

    private TimeAdapter adapter;
    private TimeInterval interval;
    private ListItemTimePreference.Type type;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_time);
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.interval = TimeInterval.CONSTANT;
        this.type = ListItemTimePreference.Type.BLOOD_SUGAR;
    }

    @Override
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        spinner = (Spinner) view.findViewById(R.id.preference_time_spinner);
        list = (RecyclerView) view.findViewById(R.id.preference_time_list);
        init();
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null) {
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.apply();
        }
    }

    private void init() {
        initSpinner();
        initList();
        loadPreferences();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_rhythm, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                interval = TimeInterval.values()[position];
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
        adapter = new TimeAdapter(getContext());
        list.setAdapter(adapter);
        updateList();
    }

    private void loadPreferences() {

    }

    private void updateList() {
        adapter.clear();
        DateTime dateTime = DateTime.now().withHourOfDay(interval.startHour);
        while (adapter.getItemCount() < DateTimeConstants.HOURS_PER_DAY / interval.interval) {
            adapter.addItem(new ListItemTimePreference(type, interval, dateTime.getHourOfDay(), 10));
            int hourOfDay = dateTime.getHourOfDay() + interval.interval;
            dateTime = dateTime.withHourOfDay(hourOfDay % DateTimeConstants.HOURS_PER_DAY);
        }
        adapter.notifyDataSetChanged();
    }
}