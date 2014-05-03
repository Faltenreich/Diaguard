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

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Filip on 14.03.14.
 */
public class TimelineFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        setHasOptionsMenu(true);
        return view;
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

        chartHelperChart = new ChartHelper(getActivity());
        chartHelperTable = new ChartHelper(getActivity());

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
        renderChart();
        setChartData();

        if(preferenceHelper.limitsAreHighlighted()) {
            renderChartLimits();
            setChartLimits();
        }

        initializeChart();
    }

    private void renderChart() {
        chartHelperChart.render();
        chartHelperChart.renderer.removeAllRenderers();

        XYSeriesRenderer seriesRendererBloodSugar = new XYSeriesRenderer();
        seriesRendererBloodSugar.setPointStyle(PointStyle.DIAMOND);
        seriesRendererBloodSugar.setColor(Color.BLACK);
        seriesRendererBloodSugar.setFillPoints(true);
        seriesRendererBloodSugar.setLineWidth(Helper.getDPI(getActivity(), 1.5f));

        seriesRendererBloodSugar.setAnnotationsColor(Color.DKGRAY);
        seriesRendererBloodSugar.setAnnotationsTextAlign(Paint.Align.CENTER);
        seriesRendererBloodSugar.setAnnotationsTextSize(Helper.getDPI(getActivity(), 14));

        chartHelperChart.renderer.addSeriesRenderer(seriesRendererBloodSugar);
        chartHelperChart.renderer.setLabelsTextSize(Helper.getDPI(getActivity(), 14));
        chartHelperChart.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 280));
    }

    private void renderChartLimits() {
        XYSeriesRenderer seriesRendererHyperglycemia = new XYSeriesRenderer();
        seriesRendererHyperglycemia.setColor(Color.argb(0, 0, 0, 0));
        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE);
        fill.setColor(Color.argb(40,252,126,126));
        seriesRendererHyperglycemia.addFillOutsideLine(fill);
        chartHelperChart.renderer.addSeriesRenderer(seriesRendererHyperglycemia);

        XYSeriesRenderer seriesRendererHypoglycemia = new XYSeriesRenderer();
        seriesRendererHypoglycemia.setLineWidth(0);
        seriesRendererHypoglycemia.setColor(Color.argb(0,0,0,0));
        fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
        fill.setColor(Color.argb(40, 126, 126, 252));
        seriesRendererHypoglycemia.addFillOutsideLine(fill);
        chartHelperChart.renderer.addSeriesRenderer(seriesRendererHypoglycemia);
    }

    private void setChartData() {
        chartHelperChart.seriesDataset.clear();

        XYSeries seriesBloodSugar = new XYSeries("Blood Sugar");
        chartHelperChart.seriesDataset.addSeries(seriesBloodSugar);

        dataSource.open();
        List<Event> bloodSugar = dataSource.getEventsOfDay(time, Event.Category.BloodSugar);
        dataSource.close();

        if(bloodSugar.size() == 0)
            return;
        else if(bloodSugar.size() > 1)
            chartHelperChart.renderer.setPointSize(0);

        float highestValue = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 260);

        for(Event event : bloodSugar) {

            float x_value = Helper.formatCalendarToHourMinutes(event.getDate());

            if(event.getValue() > highestValue)
                highestValue = event.getValue();

            float y_value = preferenceHelper.
                    formatDefaultToCustomUnit(Event.Category.BloodSugar, event.getValue());
            seriesBloodSugar.add(x_value, y_value);
            seriesBloodSugar.addAnnotation(Helper.getDecimalFormat().format(y_value), x_value, event.getValue() + Helper.getDPI(getActivity(), 3));
        }

        chartHelperChart.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, highestValue + 20));
    }

    private void setChartLimits() {
        float limitHyperglycemia = preferenceHelper.getLimitHyperglycemia();
        XYSeries seriesHyperglycemia = new XYSeries(HYPERGLYCEMIA);
        chartHelperChart.seriesDataset.addSeries(seriesHyperglycemia);
        seriesHyperglycemia.add(-10, limitHyperglycemia);
        seriesHyperglycemia.add(26, limitHyperglycemia);

        float limitHypoglycemia = preferenceHelper.getLimitHypoglycemia();
        XYSeries seriesHypoglycemia = new XYSeries(HYPOGLYCEMIA);
        chartHelperChart.seriesDataset.addSeries(seriesHypoglycemia);
        seriesHypoglycemia.add(-10, limitHypoglycemia);
        seriesHypoglycemia.add(26, limitHypoglycemia);
    }

    private void initializeChart() {
        chartHelperChart.initialize();
        chartHelperChart.chartView.addPanListener(new PanListener() {
            @Override
            public void panApplied() {
                panAppliedToEverything(true);
            }
        });
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
                        activeCategoryCount * (int)Helper.getDPI(getActivity(), 32)));
        chartHelperTable.render();
        chartHelperTable.renderer.removeAllRenderers();
        layoutTableLabels.removeAllViews();

        // Row backgrounds
        for(int row = 0; row < activeCategoryCount; row++) {
            XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
            seriesRenderer.setColor(Color.argb(0, 0, 0, 0));
            XYSeriesRenderer.FillOutsideLine fill =
                    new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE);
            if((row + activeCategoryCount) % 2 == 0)
                fill.setColor(getResources().getColor(R.color.ltgray));
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
                int resourceId = getResources().getIdentifier(category.name().toLowerCase() + "_plain",
                        "drawable", getActivity().getPackageName());
                image.setImageResource(resourceId);
                int imageSize = (int) Helper.getDPI(getActivity(), 32);
                image.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize, 1.0f));
                int imagePadding = (int) Helper.getDPI(getActivity(), 4);
                image.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
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
            XYSeries series = new XYSeries(BACKGROUND);
            chartHelperTable.seriesDataset.addSeries(series);
            series.add(-10, row);
            series.add(26, row);
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
                    String valueString = Helper.getRationalFormat().
                            format(preferenceHelper.formatDefaultToCustomUnit(category, value));

                    series.add(x_value, y_value);
                    series.addAnnotation(valueString, x_value, y_value);
                }
            }
        }
    }

    private void initializeTable() {
        chartHelperTable.initialize();
        chartHelperTable.chartView.addPanListener(new PanListener() {
            @Override
            public void panApplied() {
                panAppliedToEverything(false);
            }
        });
        layoutTableValues.removeAllViews();
        layoutTableValues.addView(chartHelperTable.chartView);
        chartHelperTable.chartView.repaint();
    }

    private void panAppliedToEverything(boolean chartIsSource) {
        ChartHelper sourceChart;
        ChartHelper affectedChart;

        if(chartIsSource) {
            sourceChart = chartHelperChart;
            affectedChart = chartHelperTable;
        }
        else {
            sourceChart = chartHelperTable;
            affectedChart = chartHelperChart;
        }
        affectedChart.renderer.setXAxisMin(sourceChart.renderer.getXAxisMin());
        affectedChart.renderer.setXAxisMax(sourceChart.renderer.getXAxisMax());
        affectedChart.renderer.setXLabels(23);

        // Repaint colored backgrounds
        for(XYSeries series : chartHelperChart.seriesDataset.getSeries()) {
            if (series.getTitle().equals(HYPERGLYCEMIA)) {
                series.clear();
                float limitHyperglycemia = preferenceHelper.getLimitHyperglycemia();
                series.add(chartHelperChart.renderer.getXAxisMin(), limitHyperglycemia);
                series.add(chartHelperChart.renderer.getXAxisMax(), limitHyperglycemia);
            }
            else if(series.getTitle().equals(HYPOGLYCEMIA)) {
                series.clear();
                float limitHypoglycemia = preferenceHelper.getLimitHypoglycemia();
                series.add(chartHelperChart.renderer.getXAxisMin(), limitHypoglycemia);
                series.add(chartHelperChart.renderer.getXAxisMax(), limitHypoglycemia);
            }
        }
        for(XYSeries series : chartHelperTable.seriesDataset.getSeries()) {
            if(series.getTitle().equals(BACKGROUND)) {
                int height = (int) series.getMaxY();
                series.clear();
                series.add(chartHelperTable.renderer.getXAxisMin(), height);
                series.add(chartHelperTable.renderer.getXAxisMax(), height);
            }
        }

        affectedChart.chartView.repaint();
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
        DialogFragment newFragment = new DatePickerFragment(time) {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                time.set(Calendar.YEAR, year);
                time.set(Calendar.MONTH, month);
                time.set(Calendar.DAY_OF_MONTH, day);
                updateContent();
            }
        };
        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
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