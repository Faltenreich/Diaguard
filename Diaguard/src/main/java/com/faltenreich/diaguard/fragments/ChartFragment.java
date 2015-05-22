package com.faltenreich.diaguard.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Model;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private static final int TEXT_SIZE = 4;
    private static final int LINE_WIDTH = 1;
    private static final int SCATTER_SHAPE_SIZE = 5;

    private PreferenceHelper preferenceHelper;
    private DatabaseDataSource dataSource;

    private LineChart viewport;
    private ScatterChart chart;

    private DateTime currentDateTime;

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void initialize() {
        preferenceHelper = new PreferenceHelper(getActivity());
        dataSource = new DatabaseDataSource(getActivity());

        currentDateTime = DateTime.now();

        getComponents();
        initializeGUI();
    }

    private void getComponents() {
        viewport = (LineChart) getView().findViewById(R.id.viewport);
        chart = (ScatterChart) getView().findViewById(R.id.chart);
    }

    private void initializeGUI() {
        setChartDefaultStyle(viewport);
        viewport.getAxisLeft().setEnabled(false);
        viewport.getXAxis().setDrawGridLines(false);

        setChartDefaultStyle(chart);
    }

    private void setChartDefaultStyle(BarLineChartBase chartBase) {
        // General
        chartBase.setDrawGridBackground(false);
        chartBase.setHighlightEnabled(false);
        chartBase.setDoubleTapToZoomEnabled(false);
        chartBase.setScaleEnabled(false);

        // Text
        chartBase.getLegend().setEnabled(false);
        chartBase.setDescription(null);
        chartBase.setNoDataText(getString(R.string.no_data));
        chartBase.getXAxis().setTextSize(Helper.getDPI(getActivity(), TEXT_SIZE));
        chartBase.getAxisLeft().setTextSize(Helper.getDPI(getActivity(), TEXT_SIZE));

        // Axes
        chartBase.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartBase.getXAxis().setDrawAxisLine(false);
        chartBase.getAxisLeft().setDrawAxisLine(false);
        chartBase.getAxisLeft().setAxisLineColor(getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisLeft().setGridColor(getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisRight().setEnabled(false);
    }

    private void update() {
        dataSource.open();

        updateViewport();
        updateChart();

        dataSource.close();
    }

    private void updateViewport() {
        ArrayList<String> xLabels = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();

        // Iterate through every day of the current selected month
        for(DateTime day = currentDateTime.dayOfMonth().withMinimumValue();
            day.isBefore(currentDateTime.dayOfMonth().withMaximumValue());
            day = day.plusDays(1)) {

            float averageOfDay = dataSource.getBloodSugarAverageOfDay(day);
            if(averageOfDay > 0) {
                averageOfDay = preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, averageOfDay);
                entries.add(new com.github.mikephil.charting.data.Entry(averageOfDay, day.getDayOfMonth()));
            }

            xLabels.add(day.getDayOfMonth() + ".");
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Viewport");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(getResources().getColor(android.R.color.darker_gray));
        lineDataSet.setLineWidth(Helper.getDPI(getActivity(), LINE_WIDTH));
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawValues(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        viewport.setData(new LineData(xLabels, dataSets));
        viewport.invalidate();
    }

    private void updateChart() {
        ArrayList<String> xLabels = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();

        for(DateTime day = currentDateTime.dayOfMonth().withMinimumValue();
            day.isBefore(currentDateTime.dayOfMonth().withMaximumValue());
            day = day.plusDays(1)) {

            String weekDay = getResources().getStringArray(R.array.weekdays_short)[day.dayOfWeek().get() - 1];

            // Create minute-precise grid for x-axis
            for(int hour = 0; hour < 24; hour++) {
                for(int minute = 0; minute < 60; minute++) {
                    xLabels.add(weekDay + ", " + day.getDayOfMonth() + "." + day.getMonthOfYear());
                }
            }

            List<Entry> entriesOfDay = dataSource.getEntriesOfDay(day);
            for(Entry entry : entriesOfDay) {
                // TODO: Generic method call
                List<Model> models = dataSource.get(DatabaseHelper.BLOODSUGAR, null,
                        DatabaseHelper.ENTRY_ID + "=?",
                        new String[]{ Long.toString(entry.getId()) },
                        null, null, null, null);
                for(Model model : models) {
                    Measurement measurement = (Measurement) model;
                    float value = preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, measurement.getValue());
                    int minutesOfMonth = day.getDayOfMonth() * 24 * 60;
                    int minutesOfHour = day.getHourOfDay() * 60;
                    int minutes = day.getMinuteOfHour();
                    entries.add(new com.github.mikephil.charting.data.Entry(value, minutesOfMonth + minutesOfHour + minutes));
                }
            }
        }

        ScatterDataSet scatterDataSet = new ScatterDataSet(entries, DatabaseHelper.BLOODSUGAR);
        scatterDataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        scatterDataSet.setScatterShapeSize(Helper.getDPI(getActivity(), SCATTER_SHAPE_SIZE));
        scatterDataSet.setColor(getResources().getColor(R.color.green));
        scatterDataSet.setDrawValues(false);

        ArrayList<ScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(scatterDataSet);

        chart.setData(new ScatterData(xLabels, dataSets));
        chart.setVisibleXRange(24 * 60);
        chart.moveViewToX(24 * 60 * (currentDateTime.getDayOfMonth() - 1));
        chart.getXAxis().setLabelsToSkip(24 * 60 - 1);

        if(preferenceHelper.limitsAreHighlighted()) {
            YAxis leftAxis = chart.getAxisLeft();

            LimitLine hyperglycemia = new LimitLine(preferenceHelper.getLimitHyperglycemia(), getString(R.string.hyper));
            hyperglycemia.setLineColor(getResources().getColor(R.color.red));
            hyperglycemia.setLabel(null);
            leftAxis.addLimitLine(hyperglycemia);

            LimitLine hypoglycemia = new LimitLine(preferenceHelper.getLimitHypoglycemia(), getString(R.string.hypo));
            hypoglycemia.setLineColor(getResources().getColor(R.color.blue));
            hypoglycemia.setLabel(null);
            leftAxis.addLimitLine(hypoglycemia);
        }

        chart.invalidate();
    }
}
