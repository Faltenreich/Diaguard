package com.faltenreich.diaguard.feature.preference.factor;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentFactorBinding;
import com.faltenreich.diaguard.feature.preference.data.TimeInterval;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.preference.FactorChangedEvent;
import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.VerticalDividerItemDecoration;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class FactorFragment extends BaseFragment<FragmentFactorBinding> implements FactorViewHolder.Callback {

    private static final int X_AXIS_MINIMUM = 0;
    private static final int X_AXIS_MAXIMUM = DateTimeConstants.HOURS_PER_DAY;

    private FactorListAdapter valuesListAdapter;

    private Factor factor;
    private TimeInterval timeInterval;
    private List<FactorItem> items;

    @Override
    protected FragmentFactorBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentFactorBinding.inflate(layoutInflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
        fetchData();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(factor.getTitle());

        initButton();
        initSpinner();
        initChart();
        initList();

        invalidateChart();
        invalidateList();
    }

    private void initArguments() {
        Bundle arguments = getActivity() != null && getActivity().getIntent() != null
            ? getActivity().getIntent().getExtras()
            : null;
        if (arguments == null) {
            throw new IllegalStateException("Arguments must not be null");
        }

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

        timeInterval = factor.getTimeInterval();
    }

    private void fetchData() {
        items = new ArrayList<>();
        for (int hourOfDay = 0; hourOfDay < DateTimeConstants.HOURS_PER_DAY; hourOfDay++) {
            FactorItem item = new FactorItem(hourOfDay, factor.getValueForHour(hourOfDay));
            items.add(item);
        }
    }

    private void initButton() {
        getBinding().fab.setOnClickListener((view) -> store());
    }

    private void initSpinner() {
        Spinner spinner = getBinding().timeIntervalSpinner;

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
                invalidateChart();
                invalidateList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void initChart() {
        LineChart chartView = getBinding().chartView;

        ChartUtils.setChartDefaultStyle(chartView, Category.INSULIN);
        chartView.setTouchEnabled(false);

        Resources resources = requireContext().getResources();
        int textColor = ColorUtils.getTextColorPrimary(getContext());
        int gridColor = ColorUtils.getBackgroundTertiary(getContext());

        chartView.getAxisLeft().setTextColor(textColor);
        chartView.getAxisLeft().setGridColor(gridColor);
        chartView.getAxisLeft().setGridLineWidth(1f);
        chartView.getAxisLeft().setXOffset(resources.getDimension(R.dimen.padding));
        chartView.getAxisLeft().setLabelCount(5);

        chartView.getXAxis().setGridLineWidth(1f);
        chartView.getXAxis().setGridColor(gridColor);
        chartView.getXAxis().setTextColor(textColor);
        chartView.getXAxis().setAxisMinimum(X_AXIS_MINIMUM);
        chartView.getXAxis().setAxisMaximum(X_AXIS_MAXIMUM);
        chartView.getXAxis().setLabelCount((X_AXIS_MAXIMUM / 2) + 1);
        chartView.getXAxis().setValueFormatter((value, axis) -> {
            boolean showValue = value < X_AXIS_MAXIMUM;
            return showValue ? Integer.toString((int) value) : "";
        });

        chartView.setViewPortOffsets(
            resources.getDimension(R.dimen.chart_offset_left),
            0,
            0,
            resources.getDimension(R.dimen.chart_offset_bottom)
        );
    }

    private void initList() {
        RecyclerView listView = getBinding().listView;
        valuesListAdapter = new FactorListAdapter(getContext(), this);
        listView.setAdapter(valuesListAdapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.addItemDecoration(new VerticalDividerItemDecoration(getContext()));
    }

    private void invalidateChart() {
        LineChart chartView = getBinding().chartView;

        List<Entry> entries = new ArrayList<>();
        for (FactorItem item : items) {
            boolean isRelevant = item.getHourOfDay() % timeInterval.rangeInHours == 0;
            if (isRelevant) {
                Entry entry = new Entry();
                entry.setX(item.getHourOfDay());
                entry.setY(item.getValue());
                entries.add(entry);
            }
        }

        // Add redundant item at the end to go full circle
        Entry lastEntry = entries.get(entries.size() - 1);
        Entry additionalEntry = new Entry();
        additionalEntry.setX(X_AXIS_MAXIMUM);
        additionalEntry.setY(lastEntry.getY());
        entries.add(additionalEntry);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet dataSet = new LineDataSet(entries, getString(factor.getTitle()));
        dataSet.setColor(ColorUtils.getPrimaryColor(getContext()));
        dataSet.setLineWidth(ChartUtils.LINE_WIDTH);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        dataSets.add(dataSet);
        LineData data = new LineData(dataSets);

        chartView.setData(data);
        chartView.invalidate();
    }

    private void invalidateList() {
        valuesListAdapter.clear();

        for (FactorItem item : items) {
            boolean isRelevant = item.getHourOfDay() % timeInterval.rangeInHours == 0;
            if (isRelevant) {
                FactorRangeItem rangeItem = new FactorRangeItem(item.getHourOfDay(), timeInterval.rangeInHours, item.getValue());
                valuesListAdapter.addItem(rangeItem);
            }
        }

        valuesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRangeItemChanged(FactorRangeItem rangeItem) {
        for (FactorItem item : items) {
            int hourStart = rangeItem.getHourOfDay();
            int hourEnd = rangeItem.getHourOfDay() + rangeItem.getRangeInHours();
            if (item.getHourOfDay() >= hourStart && item.getHourOfDay() < hourEnd) {
                item.setValue(rangeItem.getValue());
            }
        }
        invalidateChart();
    }

    private void store() {
        factor.setTimeInterval(timeInterval);

        for (FactorItem item : items) {
            factor.setValueForHour(item.getValue(), item.getHourOfDay());
        }

        Events.post(new FactorChangedEvent());

        finish();
    }
}
