package com.faltenreich.diaguard.feature.preference.factor;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.FactorChangedEvent;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import butterknife.BindView;
import butterknife.OnClick;

public class FactorFragment extends BaseFragment {

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
        initArguments();
        initTimeInterval();
        initValues();
        invalidateValues();
    }

    private void bindViews(View view) {
        this.timeIntervalSpinner = view.findViewById(R.id.time_interval_spinner);
        this.valuesList = view.findViewById(R.id.values_list);
    }

    private void initArguments() {
        Bundle arguments = getActivity() != null && getActivity().getIntent() != null
            ? getActivity().getIntent().getExtras()
            : null;
        if (arguments == null) {
            throw new IllegalStateException("Arguments must not be null");
        }
        initFactor(arguments);
    }

    private void initFactor(@NonNull Bundle arguments) {
        String key = getString(R.string.argument_factor);
        if (arguments.containsKey(key)) {
            int factorArgument = arguments.getInt(key);
            if (factorArgument == getResources().getInteger(R.integer.argument_factor_correction)) {
                factor = new CorrectionFactor();
            } else if (factorArgument == getResources().getInteger(R.integer.argument_factor_meal)) {
                factor = new MealFactor();
            } else if (factorArgument == getResources().getInteger(R.integer.argument_factor_basal_rate)) {
                factor = new BasalRateFactor();
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
        valuesListAdapter = new FactorListAdapter(getContext());
        valuesList.setAdapter(valuesListAdapter);
        valuesList.setLayoutManager(new LinearLayoutManager(getContext()));
        valuesList.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
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

    @OnClick(R.id.fab)
    void store() {
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

        Events.post(new FactorChangedEvent());

        finish();
    }
}
