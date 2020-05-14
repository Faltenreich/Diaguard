package com.faltenreich.diaguard.feature.statistic;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.feature.preference.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.primitive.FloatUtils;
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Faltenreich on 27.10.2016.
 */

public class StatisticFragment extends BaseFragment {

    private static final int MIN_MAX_Y_VALUE = 3;

    @BindView(R.id.statistics_image_category) ImageView imageViewCategory;
    @BindView(R.id.statistics_categories) Spinner spinnerCategories;
    @BindView(R.id.statistics_interval) Spinner spinnerInterval;
    @BindView(R.id.statistics_measurement_count_avg) TextView textViewMeasurementCountAvg;
    @BindView(R.id.statistics_layout_hyper) ViewGroup layoutAvgHyper;
    @BindView(R.id.statistics_avg_hyper) TextView textViewAvgHyper;
    @BindView(R.id.statistics_layout_hypo) ViewGroup layoutAvgHypo;
    @BindView(R.id.statistics_avg_hypo) TextView textViewAvgHypo;
    @BindView(R.id.statistics_avg_unit) TextView textViewAvgUnit;
    @BindView(R.id.statistics_avg_value) TextView textViewAvgValue;
    @BindView(R.id.statistics_chart_trend) LineChart chartTrend;
    @BindView(R.id.layout_distribution) ViewGroup layoutDistribution;
    @BindView(R.id.statistics_chart_distribution) PieChart chartDistribution;

    private TimeSpan timeSpan;
    private Category category;

    public StatisticFragment() {
        super(R.layout.fragment_statistic, R.string.statistics);
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
        initializeCharts();
        initSpinners();
    }

    private void initSpinners() {
        final Category[] categories = PreferenceHelper.getInstance().getActiveCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(getString(category.getStringResId()));
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeCategory(categories[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final TimeSpan[] timeSpans = TimeSpan.values();
        List<String> timeSpanNames = new ArrayList<>();
        for (TimeSpan timeSpan : timeSpans) {
            timeSpanNames.add(timeSpan.toIntervalLabel(getContext()));
        }
        ArrayAdapter<String> timeSpanAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeSpanNames);
        timeSpanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(timeSpanAdapter);
        spinnerInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeTimeInterval(timeSpans[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateContent() {
        updateViews();
        updateCharts();
    }

    private void changeTimeInterval(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
        updateContent();
    }

    private void changeCategory(Category category) {
        this.category = category;
        updateContent();
    }

    private void updateViews() {
        Interval interval = timeSpan.getInterval(DateTime.now(), -1);
        long days = interval.toDuration().getStandardDays();

        imageViewCategory.setImageResource(category.getIconImageResourceId());

        Measurement avgMeasurement = MeasurementDao.getInstance(category.toClass()).getAvgMeasurement(category, interval);
        textViewAvgUnit.setText(category.stackValues() ?
                String.format("%s %s", PreferenceHelper.getInstance().getUnitName(category), getString(R.string.per_day)) :
                PreferenceHelper.getInstance().getUnitName(category));

        textViewAvgValue.setText(avgMeasurement.toString());

        long count = EntryDao.getInstance().count(category, interval.getStart(), interval.getEnd());
        float avgCountPerDay = (float) count / (float) days;
        textViewMeasurementCountAvg.setText(FloatUtils.parseFloat(avgCountPerDay));

        if (category == Category.BLOODSUGAR) {
            layoutAvgHyper.setVisibility(View.VISIBLE);
            layoutAvgHypo.setVisibility(View.VISIBLE);
            long hyperCount = EntryDao.getInstance().countAbove(interval.getStart(), interval.getEnd(), PreferenceHelper.getInstance().getLimitHyperglycemia());
            long hypoCount = EntryDao.getInstance().countBelow(interval.getStart(), interval.getEnd(), PreferenceHelper.getInstance().getLimitHypoglycemia());
            float avgHypersPerDay = (float) hyperCount / (float) days;
            float avgHyposPerDay = (float) hypoCount / (float) days;
            textViewAvgHyper.setText(FloatUtils.parseFloat(avgHypersPerDay));
            textViewAvgHypo.setText(FloatUtils.parseFloat(avgHyposPerDay));
        } else {
            layoutAvgHyper.setVisibility(View.GONE);
            layoutAvgHypo.setVisibility(View.GONE);
        }
    }

    // region Charting

    private void initializeCharts() {
        ChartUtils.setChartDefaultStyle(chartTrend, category);

        @ColorInt int textColor = ColorUtils.getTextColorSecondary(getContext());
        chartTrend.getAxisLeft().setTextColor(textColor);
        chartTrend.getXAxis().setDrawAxisLine(true);
        chartTrend.getAxisLeft().setDrawAxisLine(false);
        chartTrend.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartTrend.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartTrend.getXAxis().setTextColor(textColor);
        chartTrend.getXAxis().setValueFormatter((value, axis) -> {
            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
            return timeSpan.getLabel(dateTime);
        });
        chartTrend.setTouchEnabled(false);

        chartDistribution.setDrawHoleEnabled(false);
        chartDistribution.setUsePercentValues(true);
        chartDistribution.setDescription(null);
        chartDistribution.setDrawEntryLabels(false);
        chartDistribution.setNoDataText(getString(ChartUtils.NO_DATA_TEXT_RESOURCE_ID));
        chartDistribution.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(getContext(), ChartUtils.NO_DATA_COLOR_RESOURCE_ID));
        chartDistribution.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        chartDistribution.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chartDistribution.getLegend().setTextColor(ColorUtils.getTextColorPrimary(getContext()));
    }


    private void updateCharts() {
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

                chartTrend.getAxisLeft().removeAllLimitLines();

                ViewGroup.LayoutParams params = chartTrend.getLayoutParams();
                params.height = hasData ? (int) getResources().getDimension(R.dimen.line_chart_height_detailed) : ViewGroup.LayoutParams.WRAP_CONTENT;
                chartTrend.setLayoutParams(params);

                if (hasData) {
                    float yAxisMinValue = PreferenceHelper.getInstance().getExtrema(category)[0] * .9f;
                    float yAxisMinCustomValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, yAxisMinValue);
                    chartTrend.getAxisLeft().setAxisMinValue(yAxisMinCustomValue);

                    float yAxisMaxCustomValue = lineData.getYMax();
                    yAxisMaxCustomValue = yAxisMaxCustomValue > MIN_MAX_Y_VALUE ? yAxisMaxCustomValue : MIN_MAX_Y_VALUE;
                    chartTrend.getAxisLeft().setAxisMaxValue(yAxisMaxCustomValue * 1.1f);

                    if (category == Category.BLOODSUGAR) {
                        float targetValue = PreferenceHelper.getInstance().
                                formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                        PreferenceHelper.getInstance().getTargetValue());
                        chartTrend.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), targetValue, R.color.green));

                        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
                            float limitHypo = PreferenceHelper.getInstance().
                                    formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                            PreferenceHelper.getInstance().getLimitHypoglycemia());
                            chartTrend.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), limitHypo, R.color.blue));
                            float limitHyper = PreferenceHelper.getInstance().
                                    formatDefaultToCustomUnit(Category.BLOODSUGAR,
                                            PreferenceHelper.getInstance().getLimitHyperglycemia());
                            chartTrend.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), limitHyper, R.color.red));
                        }
                    }

                    chartTrend.setData(lineData);
                    chartTrend.getXAxis().setAxisMinimum(0);
                    chartTrend.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
                    chartTrend.invalidate();
                } else {
                    chartTrend.clear();
                }
            }
        }).execute();

        if (category == Category.BLOODSUGAR) {
            layoutDistribution.setVisibility(View.VISIBLE);

            new BloodSugarDistributionTask(getContext(), timeSpan, pieData -> {
                if (isAdded()) {
                    boolean hasData = pieData.getDataSet().getEntryCount() > 0;
                    chartDistribution.setData(hasData ? pieData : null);
                    ViewGroup.LayoutParams params = chartDistribution.getLayoutParams();
                    params.height = hasData ? (int) getResources().getDimension(R.dimen.pie_chart_height) : ViewGroup.LayoutParams.WRAP_CONTENT;
                    chartDistribution.setLayoutParams(params);
                    chartDistribution.invalidate();
                }
            }).execute();
        } else {
            layoutDistribution.setVisibility(View.GONE);
        }
    }

    // endregion
}