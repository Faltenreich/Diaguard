package com.faltenreich.diaguard.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.ChartHelper;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip on 14.03.14.
 */
public class TimelineFragment extends Fragment {

    private final String NORMAL = "Normal";
    private final String HYPERGLYCEMIA = "Hyperglycemia";
    private final String HYPOGLYCEMIA = "Hypoglycemia";
    private final String BACKGROUND = "Background";

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;
    private ChartHelper chartHelperChart;
    private ChartHelper chartHelperTable;
    private DateTime time;

    boolean[] activeCategories;
    int activeCategoryCount;

    LinearLayout layoutChart;
    LinearLayout layoutTableValues;
    LinearLayout layoutTableLabels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // TODO: Save current date to savedInstanceState
        updateContent();
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(getActivity());
        preferenceHelper = new PreferenceHelper(getActivity());
        time = new DateTime();

        // Check all active Categories but Blood Sugar (always active in Timeline)
        Measurement.Category[] categories = Measurement.Category.values();
        activeCategories = new boolean[categories.length];
        for(int item = 0; item < categories.length; item++)
            activeCategories[item] = preferenceHelper.isCategoryActive(categories[item]);

        chartHelperChart = new ChartHelper(getActivity(), ChartHelper.ChartType.ScatterChart);
        chartHelperTable = new ChartHelper(getActivity(), ChartHelper.ChartType.LineChart);

        getComponents();
        initializeGUI();
        updateContent();
    }

    public void getComponents() {
        layoutChart = (LinearLayout) getView().findViewById(R.id.chart);
        layoutTableValues = (LinearLayout) getView().findViewById(R.id.table_values);
        layoutTableLabels = (LinearLayout) getView().findViewById(R.id.table_labels);
    }

    public void initializeGUI() {
        getView().findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
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
    }

    private int countActiveCategories() {
        int count = 0;
        for(int checkedCategory = 1; checkedCategory < activeCategories.length; checkedCategory++)
            if(activeCategories[checkedCategory])
                count++;
        return count;
    }

    private void updateContent() {
        DateTimeFormatter format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.dayOfWeek().get()-1];
        ((Button)getView().findViewById(R.id.button_date)).setText(weekDay.substring(0,2) + "., " + format.print(time));

        updateChart();

        activeCategoryCount = countActiveCategories();
        if(activeCategoryCount > 0) {
            getView().findViewById(R.id.table).setVisibility(View.VISIBLE);
            updateTable();
        }
        else
            getView().findViewById(R.id.table).setVisibility(View.GONE);
    }

    //region Chart

    private void updateChart() {
        chartHelperChart.renderer.removeAllRenderers();
        chartHelperChart.seriesDataset.clear();

        chartHelperChart.render();
        renderChart();
        setChartData();
        initializeChart();

        /*
        chartHelperChart.chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = chartHelperChart.chartView.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    double value = seriesSelection.getValue();
                    // TODO: Open NewEventActivity
                }
            }
        });
        */
    }

    private void renderChart() {
        XYSeriesRenderer seriesRenderer = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
        chartHelperChart.renderer.addSeriesRenderer(seriesRenderer);

        if(preferenceHelper.limitsAreHighlighted()) {
            XYSeriesRenderer seriesRendererHyper = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
            seriesRendererHyper.setColor(getResources().getColor(R.color.red));
            chartHelperChart.renderer.addSeriesRenderer(seriesRendererHyper);
            XYSeriesRenderer seriesRendererHypo = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
            seriesRendererHypo.setColor(getResources().getColor(R.color.blue));
            chartHelperChart.renderer.addSeriesRenderer(seriesRendererHypo);
        }

        chartHelperChart.renderer.setLabelsTextSize(Helper.getDPI(getActivity(), 14));
        chartHelperChart.renderer.setShowGrid(true);
        chartHelperChart.renderer.setShowGridY(true);
        chartHelperChart.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, 280));
    }

    private void setChartData() {
        XYSeries seriesBloodSugar = new XYSeries(NORMAL);
        chartHelperChart.seriesDataset.addSeries(seriesBloodSugar);

        XYSeries seriesBloodSugarHyper = new XYSeries(HYPERGLYCEMIA);
        XYSeries seriesBloodSugarHypo = new XYSeries(HYPOGLYCEMIA);

        if(preferenceHelper.limitsAreHighlighted()) {
            chartHelperChart.seriesDataset.addSeries(seriesBloodSugarHyper);
            chartHelperChart.seriesDataset.addSeries(seriesBloodSugarHypo);
        }

        dataSource.open();
        List<Entry> entries = dataSource.getEntriesOfDay(time, Measurement.Category.BloodSugar);
        dataSource.close();

        for(Entry entry : entries) {
            Measurement measurement = entry.getMeasurements().get(0);
            float x_value = Helper.formatCalendarToHourMinutes(entry.getDate());

            float y_value = preferenceHelper.
                    formatDefaultToCustomUnit(Measurement.Category.BloodSugar, measurement.getValue());

            // Adjust y axis
            if(y_value > chartHelperChart.renderer.getYAxisMax()) {
                chartHelperChart.renderer.setYAxisMax(preferenceHelper.
                        formatDefaultToCustomUnit(Measurement.Category.BloodSugar, measurement.getValue() + 30));
            }

            // Add value
            if(preferenceHelper.limitsAreHighlighted()) {
                if(measurement.getValue() > preferenceHelper.getLimitHyperglycemia())
                    seriesBloodSugarHyper.add(x_value, y_value);
                else if(measurement.getValue() < preferenceHelper.getLimitHypoglycemia())
                    seriesBloodSugarHypo.add(x_value, y_value);
                else
                    seriesBloodSugar.add(x_value, y_value);
            }
            else {
                seriesBloodSugar.add(x_value, y_value);
            }
        }
    }

    private void initializeChart() {
        chartHelperChart.initialize();
        layoutChart.removeAllViews();
        layoutChart.addView(chartHelperChart.chartView);
        chartHelperChart.chartView.repaint();
    }

    //endregion

    //region Table

    private void updateTable() {
        renderTable();
        setTableData();
        initializeTable();
    }

    private void renderTable() {

        getView().findViewById(R.id.table).setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        activeCategoryCount * (int) Helper.getDPI(getActivity(), 32))
        );

        layoutTableLabels.removeAllViews();
        chartHelperTable.renderer.removeAllRenderers();
        chartHelperTable.render();

        // Row backgrounds
        for(int row = 0; row < activeCategoryCount; row++) {
            XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
            seriesRenderer.setColor(Color.argb(0, 0, 0, 0));
            XYSeriesRenderer.FillOutsideLine fill =
                    new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE);
            if((row + activeCategoryCount) % 2 == 0)
                fill.setColor(getResources().getColor(R.color.gray_lt));
            else
                fill.setColor(Color.WHITE);
            seriesRenderer.addFillOutsideLine(fill);
            chartHelperTable.renderer.addSeriesRenderer(seriesRenderer);
        }

        // Table
        int activeCategoryPosition = 0;
        for(int item = 0; item < activeCategories.length; item++) {
            if(activeCategories[item] && item != Measurement.Category.BloodSugar.ordinal()) {
                Measurement.Category category = Measurement.Category.values()[item];

                // Category image
                ImageView image = new ImageView(getActivity());
                int resourceId = getResources().getIdentifier(category.name().toLowerCase(),
                        "drawable", getActivity().getPackageName());
                image.setImageResource(resourceId);
                int imageSize = (int) Helper.getDPI(getActivity(), 32);
                image.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize, 1.0f));
                int imagePadding = (int) Helper.getDPI(getActivity(), 4);
                //image.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
                layoutTableLabels.addView(image);

                XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
                seriesRenderer.setColor(Color.TRANSPARENT);
                seriesRenderer.setAnnotationsColor(Color.BLACK);
                seriesRenderer.setAnnotationsTextAlign(Paint.Align.CENTER);
                seriesRenderer.setAnnotationsTextSize(Helper.getDPI(getActivity(), 14));

                chartHelperTable.renderer.addSeriesRenderer(seriesRenderer);
                activeCategoryPosition++;
            }
        }

        chartHelperTable.renderer.setYAxisMin(0);
        chartHelperTable.renderer.setYAxisMax(activeCategoryPosition);
        chartHelperTable.renderer.setShowLabels(false);
    }

    private void setTableData() {

        chartHelperTable.seriesDataset.clear();

        // Paint rows
        for(int row = 0; row < activeCategoryCount; row++) {
            XYSeries series = new XYSeries(BACKGROUND + row);
            chartHelperTable.seriesDataset.addSeries(series);
            series.add(0 - ChartHelper.CHART_OFFSET_LEFT, row);
            series.add(24 + ChartHelper.CHART_OFFSET_RIGHT, row);
        }

        List<Measurement.Category> checkedCategoriesList = new ArrayList<Measurement.Category>();
        for(int position = 0; position < activeCategories.length; position++) {
            if(activeCategories[position] && position != Measurement.Category.BloodSugar.ordinal())
                checkedCategoriesList.add(Measurement.Category.values()[position]);
        }

        dataSource.open();
        float[][] values = dataSource.getAverageDataTable(time,
                checkedCategoriesList.toArray(new Measurement.Category[checkedCategoriesList.size()]), 12);
        dataSource.close();

        for(int categoryPosition = 0; categoryPosition < checkedCategoriesList.size(); categoryPosition++) {
            Measurement.Category category = checkedCategoriesList.get(categoryPosition);
            XYSeries series = new XYSeries(category.name());
            chartHelperTable.seriesDataset.addSeries(series);
            for(int hour = 0; hour < 12; hour++) {
                float value = values[categoryPosition][hour];
                if(value > 0) {
                    float x_value = (hour * 2) + 1;
                    float y_value = activeCategoryCount - 1 - categoryPosition + 0.3f;
                    String valueString = preferenceHelper.getDecimalFormat(Measurement.Category.BloodSugar).
                            format(preferenceHelper.formatDefaultToCustomUnit(category, value));

                    series.add(x_value, y_value);
                    series.addAnnotation(valueString, x_value, y_value);
                }
            }
        }
    }

    private void initializeTable() {
        chartHelperTable.initialize();
        layoutTableValues.removeAllViews();
        layoutTableValues.addView(chartHelperTable.chartView);
        chartHelperTable.chartView.repaint();
    }

    //endregion

    //region Listeners

    public void previousDay() {
        time = time.minusDays(1);
        updateContent();
    }

    public void nextDay() {
        time = time.plusDays(1);
        updateContent();
    }

    public void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time = time.withYear(year).withMonthOfYear(month+1).withDayOfMonth(day);
                updateContent();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }
}