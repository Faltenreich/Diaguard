package com.android.diaguard.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.ChartHelper;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;
import com.android.diaguard.helpers.ViewHelper;

import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 14.03.14.
 */
public class TimelineFragment extends Fragment {

    private final String NORMAL = "Normal";
    private final String HYPERGLYCEMIA = "Hyperglycemia";
    private final String HYPOGLYCEMIA = "Hypoglycemia";
    private final String BACKGROUND = "Background";

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    ChartHelper chartHelperChart;
    ChartHelper chartHelperTable;
    Calendar time;

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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.timeline));
        updateContent();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateContent();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    public void initialize() {
        dataSource = new DatabaseDataSource(getActivity());
        preferenceHelper = new PreferenceHelper(getActivity());
        time = Calendar.getInstance();

        // Check all active Categories but Blood Sugar (always active in Timeline)
        Event.Category[] categories = Event.Category.values();
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

        SimpleDateFormat format = preferenceHelper.getDateFormat();
        String weekDay = getResources().getStringArray(R.array.weekdays)[time.get(Calendar.DAY_OF_WEEK)-1];
        ((Button)getView().findViewById(R.id.button_date)).setText(weekDay.substring(0,2) + "., " + format.format(time.getTime()));

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

        chartHelperChart.chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = chartHelperChart.chartView.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    double value = seriesSelection.getValue();
                    ViewHelper.showToastMessage(getActivity(),
                            preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).format(value) +
                            " " + preferenceHelper.getUnitAcronym(Event.Category.BloodSugar));
                }
            }
        });
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
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 280));
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
        List<Event> bloodSugar = dataSource.getEventsOfDay(time, Event.Category.BloodSugar);
        dataSource.close();

        for(Event event : bloodSugar) {
            float x_value = Helper.formatCalendarToHourMinutes(event.getDate());

            float y_value = preferenceHelper.
                    formatDefaultToCustomUnit(Event.Category.BloodSugar, event.getValue());

            // Adjust y axis
            if(y_value > chartHelperChart.renderer.getYAxisMax()) {
                chartHelperChart.renderer.setYAxisMax(preferenceHelper.
                        formatDefaultToCustomUnit(Event.Category.BloodSugar, event.getValue() + 30));
            }

            // Add value
            if(preferenceHelper.limitsAreHighlighted()) {
                if(event.getValue() > preferenceHelper.getLimitHyperglycemia())
                    seriesBloodSugarHyper.add(x_value, y_value);
                else if(event.getValue() < preferenceHelper.getLimitHypoglycemia())
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

            if(activeCategories[item] && item != Event.Category.BloodSugar.ordinal()) {
                Event.Category category = Event.Category.values()[item];

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

        List<Event.Category> checkedCategoriesList = new ArrayList<Event.Category>();
        for(int position = 0; position < activeCategories.length; position++) {
            if(activeCategories[position] && position != Event.Category.BloodSugar.ordinal())
                checkedCategoriesList.add(Event.Category.values()[position]);
        }
        dataSource.open();
        float[][] values = dataSource.getAverageDataTable(time,
                checkedCategoriesList.toArray(new Event.Category[checkedCategoriesList.size()]), 12);
        dataSource.close();

        for(int categoryPosition = 0; categoryPosition < checkedCategoriesList.size(); categoryPosition++) {
            Event.Category category = checkedCategoriesList.get(categoryPosition);
            XYSeries series = new XYSeries(category.name());
            chartHelperTable.seriesDataset.addSeries(series);
            for(int hour = 0; hour < 12; hour++) {
                float value = values[categoryPosition][hour];
                if(value > 0) {
                    float x_value = (hour * 2) + 1;
                    float y_value = activeCategoryCount - 1 - categoryPosition + 0.3f;
                    String valueString = preferenceHelper.getDecimalFormat(Event.Category.BloodSugar).
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
        time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) - 1);
        updateContent();
    }

    public void nextDay() {
        time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH) + 1);
        updateContent();
    }

    public void showDatePicker() {
        DialogFragment fragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
                updateContent();
            }
        };
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(DatePickerFragment.DATE, time);
        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
    }

    public void startNewEventActivity() {
        Intent intent = new Intent (getActivity(), NewEventActivity.class);
        intent.putExtra("Date", time);
        startActivity(intent);
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newevent:
                startNewEventActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}