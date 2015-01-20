package com.faltenreich.diaguard.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseDataSource;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.Measurement;
import com.faltenreich.diaguard.helpers.ChartHelper;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class MainFragment extends Fragment {

    private DatabaseDataSource dataSource;
    private PreferenceHelper preferenceHelper;
    private DateTime today;

    private ViewGroup layoutLatest;
    private LinearLayout layoutChart;

    private TextView textViewLatestValue;
    private TextView textViewLatestUnit;
    private TextView textViewLatestTime;
    private TextView textViewLatestAgo;

    private TextView textViewMeasurements;
    private TextView textViewHyperglycemia;
    private TextView textViewHypoglycemia;

    private TextView textViewAverageMonth;
    private TextView textViewAverageWeek;
    private TextView textViewAverageDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponents();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void getComponents() {
        layoutLatest = (ViewGroup) getView().findViewById(R.id.layout_latest);
        layoutChart = (LinearLayout) getView().findViewById(R.id.chart);

        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestUnit = (TextView) getView().findViewById(R.id.textview_latest_unit);
        textViewLatestTime = (TextView) getView().findViewById(R.id.textview_latest_time);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        textViewMeasurements = (TextView) getView().findViewById(R.id.textview_measurements);
        textViewHyperglycemia = (TextView) getView().findViewById(R.id.textview_hyperglycemia);
        textViewHypoglycemia = (TextView) getView().findViewById(R.id.textview_hypoglycemia);

        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);
    }

    private void updateContent() {
        today = DateTime.now();

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        int countBloodSugarMeasurements = dataSource.count(
                DatabaseHelper.MEASUREMENT,
                DatabaseHelper.CATEGORY,
                Measurement.Category.BloodSugar.toString());

        if(countBloodSugarMeasurements > 0) {
            Entry entry = dataSource.getLatestBloodSugar();
            layoutLatest.setOnClickListener(null);
            textViewLatestValue.setTextSize(60);
            updateLatest(entry);
            updateDashboard();
        }
        else {
            layoutLatest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), NewEventActivity.class), MainActivity.REQUEST_EVENT_CREATED);
                }
            });
            textViewLatestValue.setTextSize(40);
            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
        }

        updateChart();

        dataSource.close();
    }

    private void updateLatest(Entry entry) {
        Measurement latestBloodSugar = entry.getMeasurements().get(0);

        // Value
        float value = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar, latestBloodSugar.getValue());
        textViewLatestValue.setText(preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(value));

        // Highlighting
        if(preferenceHelper.limitsAreHighlighted()) {
            if(latestBloodSugar.getValue() > preferenceHelper.getLimitHyperglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.red));
            else if(latestBloodSugar.getValue() < preferenceHelper.getLimitHypoglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.blue));
            else
                textViewLatestValue.setTextColor(getResources().getColor(R.color.green));
        }

        // Unit
        textViewLatestUnit.setText(preferenceHelper.getUnitAcronym(Measurement.Category.BloodSugar));

        // Time
        textViewLatestTime.setText(preferenceHelper.
                getDateFormat().print(entry.getDate()) + " " +
                Helper.getTimeFormat().print(entry.getDate()) + " | ");
        int differenceInMinutes = Minutes.minutesBetween(entry.getDate(), new DateTime()).getMinutes();

        // Highlight if last measurement is more than eight hours ago
        textViewLatestAgo.setTextColor(getResources().getColor(R.color.green));
        if(differenceInMinutes > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
    }

    private void updateDashboard() {
        updateToday();
        updateAverage();
    }

    private void updateToday() {
        int measurements = dataSource.countMeasurements(today, Measurement.Category.BloodSugar);
        textViewMeasurements.setText(Integer.toString(measurements));

        int countHypers = dataSource.countMeasurements(today,
                Measurement.Category.BloodSugar,
                preferenceHelper.getLimitHyperglycemia(), true);
        textViewHyperglycemia.setText(Integer.toString(countHypers));

        int countHypos = dataSource.countMeasurements(today,
                Measurement.Category.BloodSugar,
                preferenceHelper.getLimitHypoglycemia(), false);
        textViewHypoglycemia.setText(Integer.toString(countHypos));
    }

    private void updateAverage() {
        float avgMonth = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(30));
        float avgWeek = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(7));
        float avgDay = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(1));

        String avgMonthString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgMonth);
        if(avgMonth <= 0)
            avgMonthString = Helper.PLACEHOLDER;
        String avgWeekString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgWeek);
        if(avgWeek <= 0)
            avgWeekString = Helper.PLACEHOLDER;
        String avgDayString = preferenceHelper.
                getDecimalFormat(Measurement.Category.BloodSugar).format(avgDay);
        if(avgDay <= 0)
            avgDayString = Helper.PLACEHOLDER;

        textViewAverageMonth.setText(avgMonthString);
        textViewAverageWeek.setText(avgWeekString);
        textViewAverageDay.setText(avgDayString);
    }

    private void updateChart() {
        ChartHelper chartHelper = new ChartHelper(getActivity(), ChartHelper.ChartType.LineChart, ChartHelper.Interval.Week);
        chartHelper.renderer.removeAllRenderers();
        chartHelper.seriesDataset.clear();
        chartHelper.render();

        XYSeriesRenderer seriesRenderer = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
        seriesRenderer.setColor(getResources().getColor(R.color.green_lt));
        seriesRenderer.setFillPoints(true);
        chartHelper.renderer.addSeriesRenderer(seriesRenderer);
        chartHelper.renderer.setPointSize(Helper.getDPI(getActivity(), 3));
        chartHelper.renderer.setShowAxes(false);
        chartHelper.renderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        chartHelper.renderer.setXLabels(0);
        chartHelper.renderer.setXAxisMin(-0.5);
        chartHelper.renderer.setXAxisMax(6.5);
        chartHelper.renderer.setYLabelsColor(0, Color.argb(0x00, 0xff, 0x00, 0x00));

        XYSeries seriesBloodSugar = new XYSeries("Trend");
        chartHelper.seriesDataset.addSeries(seriesBloodSugar);

        float highestValue = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        preferenceHelper.getLimitHyperglycemia());
        // Set labels
        int count = 0;
        final int daysOfWeek = 7;
        for(int pastDayFromNow = 0; pastDayFromNow < daysOfWeek; pastDayFromNow++) {
            DateTime day = today.minusDays(pastDayFromNow);
            int x_value = daysOfWeek - pastDayFromNow - 1;

            // Set label
            String weekDay = pastDayFromNow == 0 ?
                    getString(R.string.today) :
                    getResources().getStringArray(R.array.weekdays_short)[day.dayOfWeek().get() - 1];
            chartHelper.renderer.addXTextLabel(x_value, weekDay);

            // Insert average
            float averageOfDay = dataSource.getBloodSugarAverageOfDay(day);
            if(averageOfDay > 0) {
                float y_value = preferenceHelper.
                        formatDefaultToCustomUnit(Measurement.Category.BloodSugar, averageOfDay);

                // Adjust y axis
                if(y_value > highestValue) {
                    highestValue = y_value;
                }

                seriesBloodSugar.add(x_value, y_value);
                count++;
            }
        }
        chartHelper.renderer.setYAxisMax(highestValue +
                preferenceHelper.formatDefaultToCustomUnit(Measurement.Category.BloodSugar, 30));
        if(count >= 2) {
            seriesRenderer.setPointStyle(PointStyle.POINT);
        }

        // Orientation lines
        chartHelper.renderer.setGridColor(getResources().getColor(R.color.green_lt));
        chartHelper.renderer.setShowCustomTextGridY(true);
        chartHelper.renderer.setShowGridX(true);
        chartHelper.renderer.setYLabels(0);
        float targetValue = preferenceHelper.
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        preferenceHelper.getTargetValue());
        chartHelper.renderer.addYTextLabel(targetValue, "1");
        chartHelper.renderer.setYAxisMin(0);

        chartHelper.initialize();
        layoutChart.removeAllViews();
        layoutChart.addView(chartHelper.chartView);
        chartHelper.chartView.repaint();
    }
}
