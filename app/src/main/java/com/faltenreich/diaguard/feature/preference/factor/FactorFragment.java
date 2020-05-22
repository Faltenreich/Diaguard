package com.faltenreich.diaguard.feature.preference.factor;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import butterknife.BindView;

public class FactorFragment extends BaseFragment {

    private static final String ARGUMENT_FACTOR = "factor";

    private static final int FACTOR_CORRECTION = 0;
    private static final int FACTOR_MEAL = 1;
    private static final int FACTOR_BASAL_RATE = 2;

    @BindView(R.id.time_interval_spinner) Spinner timeIntervalSpinner;
    @BindView(R.id.values_list) RecyclerView valuesList;

    private FactorListAdapter valuesListAdapter;
    private Factor factor;

    private TimeInterval timeInterval;

    public FactorFragment() {
        super(R.layout.fragment_factor);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initFactor();
        initTimeInterval();
        initValues();
        invalidateValues();
    }

    private void bindViews(View view) {
        this.timeIntervalSpinner = view.findViewById(R.id.time_interval_spinner);
        this.valuesList = view.findViewById(R.id.values_list);
    }

    private void initFactor() {
        // TODO: Remove test code
        factor = new CorrectionFactor();

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARGUMENT_FACTOR)) {
            int factorArgument = arguments.getInt(ARGUMENT_FACTOR);
            switch (factorArgument) {
                case FACTOR_CORRECTION:
                    factor = new CorrectionFactor();
                    break;
                case FACTOR_MEAL:
                    factor = new MealFactor();
                    break;
                case FACTOR_BASAL_RATE:
                    // TODO
                    break;
            }
        }

        if (factor == null) {
            throw new IllegalStateException("Factor must not be null");
        }

        setTitle(factor.getTitle());
    }

    private void initTimeInterval() {
        this.timeInterval = factor.getTimeInterval();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_rhythm,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeIntervalSpinner.setAdapter(adapter);

        if (timeInterval.ordinal() < timeIntervalSpinner.getCount()) {
            timeIntervalSpinner.setSelection(timeInterval.ordinal());
        }

        timeIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                timeInterval = TimeInterval.values()[position];
                invalidateValues();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initValues() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        valuesList.setLayoutManager(layoutManager);
        valuesListAdapter = new FactorListAdapter(getContext());
        valuesList.setAdapter(valuesListAdapter);
    }

    private void invalidateValues() {
        valuesListAdapter.clear();
        DateTime dateTime = DateTime.now().withHourOfDay(timeInterval.startHour);
        while (valuesListAdapter.getItemCount() < DateTimeConstants.HOURS_PER_DAY / timeInterval.interval) {
            int hourOfDay = dateTime.getHourOfDay();
            valuesListAdapter.addItem(new FactorListItem(timeInterval, hourOfDay, factor.getValueForHour(hourOfDay)));
            dateTime = dateTime.withHourOfDay((hourOfDay + timeInterval.interval) % DateTimeConstants.HOURS_PER_DAY);
        }
        valuesListAdapter.notifyDataSetChanged();
    }

    private void store() {
        factor.setTimeInterval(timeInterval);

        for (int pos = 0; pos < valuesListAdapter.getItemCount(); pos++) {
            FactorListItem item = valuesListAdapter.getItem(pos);
            int hoursIntoInterval = 0;

            while (hoursIntoInterval < item.getInterval().interval) {
                int hourOfDay = (item.getHourOfDay() + hoursIntoInterval) % DateTimeConstants.HOURS_PER_DAY;
                factor.setValueForHour(item.getValue(), hourOfDay);
                hoursIntoInterval++;
            }
        }
    }
}
