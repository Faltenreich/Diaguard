package com.android.diaguard.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.PreferencesActivity;
import com.android.diaguard.R;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.ChartHelper;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment {

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DecimalFormat format;
    ChartHelper chartHelper;

    TextView textViewLatestValue;
    TextView textViewLatestAgo;
    ImageView imageViewTrend;
    TextView textViewAverageMonth;
    TextView textViewAverageWeek;
    TextView textViewAverageDay;
    LinearLayout linearLayoutChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
        getActivity().setTitle(getString(R.string.app_name));
        updateContent();
    }

    private void initialize()  {
        getComponents();

        getView().findViewById(R.id.layout_newevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventActivity.class));
            }
        });

        getView().findViewById(R.id.layout_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(MainActivity.FragmentType.Timeline);
            }
        });
    }

    private void getComponents() {
        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        imageViewTrend = (ImageView) getView().findViewById(R.id.imageview_trend);
        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);

        linearLayoutChart = (LinearLayout) getView().findViewById(R.id.layout_chart);
    }

    private void updateContent() {

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        if(dataSource.countEvents(Event.Category.BloodSugar) > 0) {
            format = Helper.getDecimalFormat();
            setBoxCurrent();
            setBoxTrend();

            dataSource.close();
        }
        else {
            textViewLatestValue.setText(Helper.PLACEHOLDER);
            textViewLatestAgo.setText(getString(R.string.notyet));

            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
        }
        setBoxToday();
    }

    private void setBoxCurrent() {
        Calendar now = Calendar.getInstance();
        Event latestEvent = dataSource.getLatestEvent(Event.Category.BloodSugar);

        float value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, latestEvent.getValue());
        textViewLatestValue.setText(format.format(value));

        int difference = Helper.getDifferenceInMinutes(latestEvent.getDate(), now);

        textViewLatestAgo.setTextColor(getResources().getColor(android.R.color.darker_gray));
        // Highlight if last measurement is more than eight hours ago
        if(difference < 2) {
            textViewLatestAgo.setText(getString(R.string.latest_moments));
            return;
        }
        else if(difference > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        String textAgo = getString(R.string.latest);
        if(difference > 2879) {
            difference = difference / 60 / 24;
            textAgo = textAgo.replace("[unit]", getString(R.string.days));
        }
        else if(difference > 119) {
            difference = difference / 60;
            textAgo = textAgo.replace("[unit]", getString(R.string.hours));
        }
        else {
            textAgo = textAgo.replace("[unit]", getString(R.string.minutes));
        }
        textAgo = textAgo.replace("[value]", Integer.toString(difference));

        textViewLatestAgo.setText(textAgo);
    }

    private void setBoxTrend() {
        float avgMonth = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(30));
        float avgWeek = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(7));
        float avgDay = preferenceHelper.
                formatDefaultToCustomUnit(Event.Category.BloodSugar,
                        dataSource.getBloodSugarAverage(1));

        if(avgMonth > 20)
            format = new DecimalFormat("#");

        String avgMonthString = format.format(avgMonth);
        if(avgMonth <= 0)
            avgMonthString = Helper.PLACEHOLDER;
        String avgWeekString = format.format(avgWeek);
        if(avgWeek <= 0)
            avgWeekString = Helper.PLACEHOLDER;
        String avgDayString = format.format(avgDay);
        if(avgDay <= 0)
            avgDayString = Helper.PLACEHOLDER;

        textViewAverageMonth.setText(avgMonthString);
        textViewAverageWeek.setText(avgWeekString);
        textViewAverageDay.setText(avgDayString);

        // Trend arrow
        float targetValue = preferenceHelper.getTargetValue();
        float monthOffset = (targetValue - avgMonth) * (-1); // how far from good?
        float weekOffset = (targetValue - avgWeek) * (-1);
        float difference = monthOffset - weekOffset; // the higher the better, negative is worse
        // which difference should be visualized?
        float sensitivity = 30 * preferenceHelper.getUnitValue(Event.Category.BloodSugar);

        // TODO: Infinite adjustment, better calculation, testing, tip
        if(difference > sensitivity) {
            imageViewTrend.setImageResource(R.drawable.arrow_up);
        }
        else if(difference < (-sensitivity)) {
            imageViewTrend.setImageResource(R.drawable.arrow_down);
        }
        else {
            imageViewTrend.setImageResource(R.drawable.arrow_neutral);
        }
    }

    private void setBoxToday() {
        ChartTask chartTask = new ChartTask();
        chartTask.execute();
    }

    private void renderChart() {
        chartHelper.render();
        chartHelper.renderer.removeAllRenderers();

        XYSeriesRenderer seriesRendererBloodSugar = new XYSeriesRenderer();
        seriesRendererBloodSugar.setPointStyle(PointStyle.DIAMOND);
        seriesRendererBloodSugar.setColor(Color.BLACK);
        seriesRendererBloodSugar.setFillPoints(true);
        seriesRendererBloodSugar.setLineWidth(Helper.getDPI(getActivity(), 1.5f));

        chartHelper.renderer.addSeriesRenderer(seriesRendererBloodSugar);
        chartHelper.renderer.setLabelsTextSize(Helper.getDPI(getActivity(), 14));
        chartHelper.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 280));
        chartHelper.renderer.setShowAxes(false);
        chartHelper.renderer.setShowLabels(false);
        chartHelper.renderer.setShowGrid(false);
        chartHelper.renderer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        chartHelper.renderer.setMargins(new int[]{0, 0, 0, 0});

        renderChartLimits();
    }

    private void renderChartLimits() {
        XYSeriesRenderer seriesRendererHyperglycemia = new XYSeriesRenderer();
        seriesRendererHyperglycemia.setColor(Color.argb(0, 0, 0, 0));
        XYSeriesRenderer.FillOutsideLine fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.ABOVE);
        fill.setColor(Color.argb(40, 252, 126, 126));
        seriesRendererHyperglycemia.addFillOutsideLine(fill);
        chartHelper.renderer.addSeriesRenderer(seriesRendererHyperglycemia);

        XYSeriesRenderer seriesRendererHypoglycemia = new XYSeriesRenderer();
        seriesRendererHypoglycemia.setLineWidth(0);
        seriesRendererHypoglycemia.setColor(Color.argb(0, 0, 0, 0));
        fill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BELOW);
        fill.setColor(Color.argb(40, 126, 126, 252));
        seriesRendererHypoglycemia.addFillOutsideLine(fill);
        chartHelper.renderer.addSeriesRenderer(seriesRendererHypoglycemia);
    }

    private void setChartData() {
        chartHelper.seriesDataset.clear();

        XYSeries seriesBloodSugar = new XYSeries("Blood Sugar");
        chartHelper.seriesDataset.addSeries(seriesBloodSugar);

        DatabaseDataSource dataSource1 = new DatabaseDataSource(getActivity());
        dataSource1.open();
        List<Event> bloodSugarOfDay = dataSource1.getEventsOfDay(Calendar.getInstance(), Event.Category.BloodSugar);
        dataSource1.close();

        if(bloodSugarOfDay.size() > 1)
            chartHelper.renderer.setPointSize(0);

        float rangeMaximum =
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 260);
        float highestValue = rangeMaximum;

        for(Event event : bloodSugarOfDay) {

            float x_value = Helper.formatCalendarToHourMinutes(event.getDate());

            if(event.getValue() > highestValue)
                highestValue = event.getValue();

            seriesBloodSugar.add(x_value, preferenceHelper.
                    formatDefaultToCustomUnit(Event.Category.BloodSugar, event.getValue()));
        }

        chartHelper.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, highestValue + 20));

        setChartLimits();
    }

    private void setChartLimits() {
        float limitHyperglycemia = preferenceHelper.getLimitHyperglycemia();
        XYSeries seriesHyperglycemia = new XYSeries("Hyperglycemia");
        chartHelper.seriesDataset.addSeries(seriesHyperglycemia);
        seriesHyperglycemia.add(-10, limitHyperglycemia);
        seriesHyperglycemia.add(26, limitHyperglycemia);

        float limitHypoglycemia = preferenceHelper.getLimitHypoglycemia();
        XYSeries seriesHypoglycemia = new XYSeries("Hypoglycemia");
        chartHelper.seriesDataset.addSeries(seriesHypoglycemia);
        seriesHypoglycemia.add(-10, limitHypoglycemia);
        seriesHypoglycemia.add(26, limitHypoglycemia);
    }

    private void initializeChart() {
        chartHelper.initialize();
        linearLayoutChart.removeAllViews();
        linearLayoutChart.addView(chartHelper.chartView);
        chartHelper.chartView.repaint();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent (getActivity(), PreferencesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ChartTask extends AsyncTask<Void, Void, Void> {
        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected Void doInBackground(Void... urls) {
            chartHelper = new ChartHelper(getActivity());
            renderChart();
            setChartData();
            return null;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(Void param) {
            initializeChart();
        }
    }
}
