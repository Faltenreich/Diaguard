package com.faltenreich.diaguard.feature.preference.factor;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import butterknife.BindView;

public class FactorPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    public static FactorPreferenceDialogFragment newInstance(String key) {
        FactorPreferenceDialogFragment fragment = new FactorPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        fragment.setArguments(arguments);
        return fragment;
    }

    @BindView(R.id.preference_time_spinner) Spinner spinner;
    @BindView(R.id.preference_time_list) RecyclerView list;

    private FactorListAdapter adapter;
    private TimeInterval timeInterval;

    private FactorPreferenceDialogFragment() {
        super();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        bindViews(view);
        initLayout();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(shownDialog -> {
            // Workaround for showing keyboard on focusing EditText in RecyclerView
            Dialog dialogWithWindow = (Dialog) shownDialog;
            if (dialogWithWindow.getWindow() != null) {
                int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
                dialogWithWindow.getWindow().clearFlags(flags);
            }
        });
        return dialog;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            store();
        }
    }

    private void bindViews(View view) {
        this.spinner = view.findViewById(R.id.preference_time_spinner);
        this.list = view.findViewById(R.id.preference_time_list);
    }

    private void initLayout() {
        FactorPreference preference = (FactorPreference) getPreference();
        this.timeInterval = preference.getTimeInterval();
        initSpinner();
        initList();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_rhythm,
            android.R.layout.simple_spinner_item
        );
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
            public void onNothingSelected(AdapterView<?> adapterView) {}
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
        FactorPreference preference = (FactorPreference) getPreference();
        adapter.clear();
        DateTime dateTime = DateTime.now().withHourOfDay(timeInterval.startHour);
        while (adapter.getItemCount() < DateTimeConstants.HOURS_PER_DAY / timeInterval.interval) {
            int hourOfDay = dateTime.getHourOfDay();
            adapter.addItem(new FactorListItem(timeInterval, hourOfDay, preference.getValueForHour(hourOfDay)));
            dateTime = dateTime.withHourOfDay((hourOfDay + timeInterval.interval) % DateTimeConstants.HOURS_PER_DAY);
        }
        adapter.notifyDataSetChanged();
    }

    private void store() {
        FactorPreference preference = (FactorPreference) getPreference();
        preference.setTimeInterval(timeInterval);

        for (int pos = 0; pos < adapter.getItemCount(); pos++) {
            FactorListItem item = adapter.getItem(pos);
            int hoursIntoInterval = 0;

            while (hoursIntoInterval < item.getInterval().interval) {
                int hourOfDay = (item.getHourOfDay() + hoursIntoInterval) % DateTimeConstants.HOURS_PER_DAY;
                preference.storeValueForHour(item.getValue(), hourOfDay);
                hoursIntoInterval++;
            }
        }

        preference.onPreferenceUpdate();
    }
}