package com.faltenreich.diaguard.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment implements OnChartValueSelectedListener {

    private static final int TEXT_SIZE = 4;
    private static final int LINE_WIDTH = 1;
    private static final int SCATTER_SHAPE_SIZE = 5;

    private PreferenceHelper preferenceHelper;
    private DatabaseDataSource dataSource;

    private LineChart viewport;
    private ScatterChart chart;
    Button buttonDate;

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
        buttonDate = (Button) getView().findViewById(R.id.button_date);
    }

    private void initializeGUI() {
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        getView().findViewById(R.id.button_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDay();
            }
        });

        getView().findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDay();
            }
        });

        setChartDefaultStyle(viewport);
        viewport.getAxisLeft().setEnabled(false);
        viewport.getXAxis().setDrawGridLines(false);

        setChartDefaultStyle(chart);
        chart.setOnChartValueSelectedListener(this);
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

        // TODO: Only for large devices
        // updateViewport();
        updateChartData();
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
        DateTimeFormatter format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[currentDateTime.dayOfWeek().get()-1];
        String dateButtonText = weekDay.substring(0,2) + "., " + format.print(currentDateTime);
        buttonDate.setText(dateButtonText);

        moveViewportToCurrentDateTime();
    }

    private void moveViewportToCurrentDateTime(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                int xPosition = chart.getLowestVisibleXIndex();
                final int xPositionTarget = ((currentDateTime.getDayOfMonth() - 1) * 24 * 60);
                final int distanceInMinutes = xPositionTarget - xPosition;
                boolean hasReachedTarget = false;
                while (!hasReachedTarget) {
                    final int currentPosition = xPosition + (distanceInMinutes / 60);
                    xPosition = currentPosition;
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            chart.moveViewToX(currentPosition);
                            chart.invalidate();
                        }
                    });
                    hasReachedTarget = distanceInMinutes > 0 ?
                            currentPosition > xPositionTarget :
                            currentPosition < xPositionTarget;
                }
            }
        };
        new Thread(runnable).start();
    }

    private void updateChartData() {
        ArrayList<String> xLabels = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();

        for(DateTime day = currentDateTime.dayOfMonth().withMinimumValue();
            day.isBefore(currentDateTime.dayOfMonth().withMaximumValue().plusDays(1));
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
                    int minutesOfMonth = (entry.getDate().getDayOfMonth() - 1) * 24 * 60;
                    int minutesOfHour = entry.getDate().getHourOfDay() * 60;
                    int minutes = entry.getDate().getMinuteOfHour();
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
        chart.getXAxis().setLabelsToSkip(24 * 60);

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

    //region Listeners

    public void previousDay() {
        currentDateTime = currentDateTime.minusDays(1);
        updateChart();
    }

    public void nextDay() {
        currentDateTime = currentDateTime.plusDays(1);
        updateChart();
    }

    public void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                currentDateTime = currentDateTime.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                updateChart();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, currentDateTime);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry.
     * @param dataSetIndex The index in the datasets array of the data object
     * the Entrys DataSet is in.
     * @param h the corresponding highlight object that contains information
     * about the highlighted position
     */
    @Override
    public void onValueSelected(com.github.mikephil.charting.data.Entry e, int dataSetIndex, Highlight h) {
        ChartMarkerView mv = new ChartMarkerView (getActivity());
        chart.setMarkerView(mv);
    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {

    }

    //endregion
}
