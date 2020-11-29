package com.faltenreich.diaguard.feature.statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentStatisticBinding;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class StatisticFragment extends BaseFragment<FragmentStatisticBinding> implements ToolbarDescribing {

    private static final int MIN_MAX_Y_VALUE = 3;

    private TimeSpan timeSpan;
    private Category category;

    public StatisticFragment() {
        super(R.layout.fragment_statistic);
    }

    @Override
    protected FragmentStatisticBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentStatisticBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.statistics)
            .build();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void init() {
        this.timeSpan = TimeSpan.WEEK;
        this.category = Category.BLOODSUGAR;
        initLayout();
    }

    private void initLayout() {
        initCategory();
        initInterval();
        initTrend();
        initDistribution();
    }

    private void initCategory() {
        Spinner spinner = getBinding().categorySpinner;
        Category[] categories = PreferenceStore.getInstance().getActiveCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(getString(category.getStringResId()));
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeCategory(categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initInterval() {
        Spinner spinner = getBinding().intervalSpinner;
        TimeSpan[] timeSpans = TimeSpan.values();
        List<String> timeSpanNames = new ArrayList<>();
        for (TimeSpan timeSpan : timeSpans) {
            timeSpanNames.add(timeSpan.toIntervalLabel(requireContext()));
        }
        ArrayAdapter<String> timeSpanAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, timeSpanNames);
        timeSpanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(timeSpanAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTimeInterval(timeSpans[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initTrend() {
        LineChart chartView = getBinding().trendChartView;
        ChartUtils.setChartDefaultStyle(chartView, category);

        @ColorInt int textColor = ColorUtils.getTextColorSecondary(getContext());
        chartView.getAxisLeft().setTextColor(textColor);
        chartView.getXAxis().setDrawAxisLine(true);
        chartView.getAxisLeft().setDrawAxisLine(false);
        chartView.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartView.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartView.getXAxis().setTextColor(textColor);
        chartView.getXAxis().setValueFormatter((value, axis) -> {
            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
            return timeSpan.getLabel(dateTime);
        });
        chartView.setTouchEnabled(false);
    }

    private void initDistribution() {
        PieChart chartView = getBinding().distributionChartView;
        chartView.setDrawHoleEnabled(false);
        chartView.setUsePercentValues(true);
        chartView.setDescription(null);
        chartView.setDrawEntryLabels(false);
        chartView.setNoDataText(getString(ChartUtils.NO_DATA_TEXT_RESOURCE_ID));
        chartView.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(requireContext(), ChartUtils.NO_DATA_COLOR_RESOURCE_ID));
        chartView.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartView.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartView.getLegend().setTextColor(ColorUtils.getTextColorPrimary(getContext()));
    }

    private void changeTimeInterval(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
        updateContent();
    }

    private void changeCategory(Category category) {
        this.category = category;
        updateContent();
    }

    private void updateContent() {
        invalidateAverage();
        invalidateTrend();
        invalidateDistribution();
    }

    private void invalidateAverage() {
        Interval interval = timeSpan.getInterval(DateTime.now(), -1);
        long days = interval.toDuration().getStandardDays();

        getBinding().categoryImageView.setImageResource(category.getIconImageResourceId());

        Measurement avgMeasurement = MeasurementDao.getInstance(category.toClass()).getAvgMeasurement(category, interval);
        getBinding().averageUnitLabel.setText(category.stackValues() ?
                String.format("%s %s", PreferenceStore.getInstance().getUnitName(category), getString(R.string.per_day)) :
                PreferenceStore.getInstance().getUnitName(category));

        getBinding().averageValueLabel.setText(avgMeasurement.toString());

        long count = EntryDao.getInstance().count(category, interval.getStart(), interval.getEnd());
        float avgCountPerDay = (float) count / (float) days;
        getBinding().measurementCountAverageLabel.setText(FloatUtils.parseFloat(avgCountPerDay));

        if (category == Category.BLOODSUGAR) {
            getBinding().hypergylcemiaCountAverageLayout.setVisibility(View.VISIBLE);
            getBinding().hypogylcemiaCountAverageLayout.setVisibility(View.VISIBLE);
            long hyperCount = EntryDao.getInstance().countAbove(interval.getStart(), interval.getEnd(), PreferenceStore.getInstance().getLimitHyperglycemia());
            long hypoCount = EntryDao.getInstance().countBelow(interval.getStart(), interval.getEnd(), PreferenceStore.getInstance().getLimitHypoglycemia());
            float avgHypersPerDay = (float) hyperCount / (float) days;
            float avgHyposPerDay = (float) hypoCount / (float) days;
            getBinding().hypergylcemiaCountAverageLabel.setText(FloatUtils.parseFloat(avgHypersPerDay));
            getBinding().hypogylcemiaCountAverageLabel.setText(FloatUtils.parseFloat(avgHyposPerDay));
        } else {
            getBinding().hypergylcemiaCountAverageLayout.setVisibility(View.GONE);
            getBinding().hypogylcemiaCountAverageLayout.setVisibility(View.GONE);
        }
    }

    private void invalidateTrend() {
        new MeasurementAverageTask(getContext(), category, timeSpan, false, true, lineData -> {
            if (isAdded()) {
                boolean hasData = false;
                if (lineData != null) {
                    for (ILineDataSet lineDataSet : lineData.getDataSets()) {
                        if (lineDataSet.getEntryCount() > 0) {
                            hasData = true;
                            break;
                        }
                    }
                }

                LineChart chartView = getBinding().trendChartView;
                chartView.getAxisLeft().removeAllLimitLines();

                ViewGroup.LayoutParams params = chartView.getLayoutParams();
                params.height = hasData ? (int) getResources().getDimension(R.dimen.line_chart_height_detailed) : ViewGroup.LayoutParams.WRAP_CONTENT;
                chartView.setLayoutParams(params);

                if (hasData) {
                    float yAxisMinValue = PreferenceStore.getInstance().getExtrema(category)[0] * .9f;
                    float yAxisMinCustomValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(category, yAxisMinValue);
                    chartView.getAxisLeft().setAxisMinValue(yAxisMinCustomValue);

                    float yAxisMaxCustomValue = lineData.getYMax();
                    yAxisMaxCustomValue = yAxisMaxCustomValue > MIN_MAX_Y_VALUE ? yAxisMaxCustomValue : MIN_MAX_Y_VALUE;
                    chartView.getAxisLeft().setAxisMaxValue(yAxisMaxCustomValue * 1.1f);

                    if (category == Category.BLOODSUGAR) {
                        float targetValue = PreferenceStore.getInstance().
                            formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                PreferenceStore.getInstance().getTargetValue());
                        chartView.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), targetValue, R.color.green));

                        if (PreferenceStore.getInstance().limitsAreHighlighted()) {
                            float limitHypo = PreferenceStore.getInstance().
                                formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                    PreferenceStore.getInstance().getLimitHypoglycemia());
                            chartView.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), limitHypo, R.color.blue));
                            float limitHyper = PreferenceStore.getInstance().
                                formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                    PreferenceStore.getInstance().getLimitHyperglycemia());
                            chartView.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), limitHyper, R.color.red));
                        }
                    }

                    chartView.setData(lineData);
                    chartView.getXAxis().setAxisMinimum(0);
                    chartView.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
                    chartView.invalidate();
                } else {
                    chartView.clear();
                }
            }
        }).execute();
    }

    private void invalidateDistribution() {
        if (category == Category.BLOODSUGAR) {
            getBinding().distributionLayout.setVisibility(View.VISIBLE);
            new BloodSugarDistributionTask(getContext(), timeSpan, pieData -> {
                if (isAdded()) {
                    PieChart chartView = getBinding().distributionChartView;
                    boolean hasData = pieData.getDataSet().getEntryCount() > 0;
                    chartView.setData(hasData ? pieData : null);
                    ViewGroup.LayoutParams params = chartView.getLayoutParams();
                    params.height = hasData ? (int) getResources().getDimension(R.dimen.pie_chart_height) : ViewGroup.LayoutParams.WRAP_CONTENT;
                    chartView.setLayoutParams(params);
                    chartView.invalidate();
                }
            }).execute();
        } else {
            getBinding().distributionLayout.setVisibility(View.GONE);
        }
    }
}