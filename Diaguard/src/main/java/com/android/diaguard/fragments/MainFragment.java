package com.android.diaguard.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.diaguard.CalculatorActivity;
import com.android.diaguard.MainActivity;
import com.android.diaguard.NewEventActivity;
import com.android.diaguard.R;
import com.android.diaguard.adapters.ListViewAdapterMain;
import com.android.diaguard.database.DatabaseDataSource;
import com.android.diaguard.database.Event;
import com.android.diaguard.helpers.ChartHelper;
import com.android.diaguard.helpers.Helper;
import com.android.diaguard.helpers.PreferenceHelper;

import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment {

    private final String NORMAL = "Normal";
    private final String HYPERGLYCEMIA = "Hyperglycemia";
    private final String HYPOGLYCEMIA = "Hypoglycemia";

    DatabaseDataSource dataSource;
    PreferenceHelper preferenceHelper;
    DecimalFormat format;
    ChartHelper chartHelper;

    TextView textViewLatestValue;
    TextView textViewLatestAgo;
    TextView textViewAverageMonth;
    TextView textViewAverageWeek;
    TextView textViewAverageDay;
    LinearLayout linearLayoutChart;
    ListView listViewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
        updateContent();
    }

    private void getComponents() {
        textViewLatestValue = (TextView) getView().findViewById(R.id.textview_latest_value);
        textViewLatestAgo = (TextView) getView().findViewById(R.id.textview_latest_ago);

        textViewAverageMonth = (TextView) getView().findViewById(R.id.textview_avg_month);
        textViewAverageWeek = (TextView) getView().findViewById(R.id.textview_avg_week);
        textViewAverageDay = (TextView) getView().findViewById(R.id.textview_avg_day);

        linearLayoutChart = (LinearLayout) getView().findViewById(R.id.chartview);

        listViewEvents = (ListView) getView().findViewById(R.id.listview);
    }

    private void updateContent() {

        getView().findViewById(R.id.image_newevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewEventActivity.class));
            }
        });

        getView().findViewById(R.id.image_calculator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CalculatorActivity.class));
            }
        });

        getView().findViewById(R.id.layout_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(MainActivity.FragmentType.Timeline);
            }
        });

        dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();

        preferenceHelper = new PreferenceHelper(getActivity());

        List<Event> lastEvents = dataSource.getEventsOfDay(Calendar.getInstance());
        ListViewAdapterMain adapter = new ListViewAdapterMain(getActivity());
        adapter.events.addAll(lastEvents);
        listViewEvents.setAdapter(adapter);

        if(dataSource.countEvents(Event.Category.BloodSugar) > 0) {
            textViewLatestValue.setTextSize(34);
            format = Helper.getDecimalFormat();
            setBoxLatest();
            setBoxAverages();

            ChartTask chartTask = new ChartTask();
            chartTask.execute();
        }
        else {
            textViewAverageMonth.setText(Helper.PLACEHOLDER);
            textViewAverageWeek.setText(Helper.PLACEHOLDER);
            textViewAverageDay.setText(Helper.PLACEHOLDER);
            textViewLatestValue.setTextSize(24);

            getView().findViewById(R.id.layout_latest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewEventActivity.class);
                    startActivity(intent);
                }
            });
        }

        dataSource.close();
    }

    private void setBoxLatest() {

        final Event latestEvent = dataSource.getLatestEvent(Event.Category.BloodSugar);

        float value = preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, latestEvent.getValue());
        textViewLatestValue.setText(format.format(value));

        int differenceInMinutes = Helper.getDifferenceInMinutes(latestEvent.getDate(), Calendar.getInstance());

        // Highlight if last measurement is more than eight hours ago
        textViewLatestAgo.setTextColor(getResources().getColor(android.R.color.darker_gray));
        if(differenceInMinutes > 480)
            textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));

        textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));

        getView().findViewById(R.id.layout_latest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                intent.putExtra("ID", latestEvent.getId());
                startActivity(intent);
            }
        });
    }

    private void setBoxAverages() {
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
    }

    // region Chart

    private void updateChart() {
        chartHelper = new ChartHelper(getActivity(), ChartHelper.ChartType.ScatterChart);
        renderChart();
        setChartData();
    }

    private void renderChart() {
        chartHelper.renderer.removeAllRenderers();
        chartHelper.render();

        XYSeriesRenderer seriesRenderer = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
        chartHelper.renderer.addSeriesRenderer(seriesRenderer);

        if(preferenceHelper.limitsAreHighlighted()) {
            XYSeriesRenderer seriesRendererHyper = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
            seriesRendererHyper.setColor(getResources().getColor(R.color.red));
            chartHelper.renderer.addSeriesRenderer(seriesRendererHyper);
            XYSeriesRenderer seriesRendererHypo = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
            seriesRendererHypo.setColor(getResources().getColor(R.color.blue));
            chartHelper.renderer.addSeriesRenderer(seriesRendererHypo);
        }

        chartHelper.renderer.setYAxisMax(
                preferenceHelper.formatDefaultToCustomUnit(Event.Category.BloodSugar, 280));
        chartHelper.renderer.setShowAxes(false);
        chartHelper.renderer.setShowLabels(false);
    }

    private void setChartData() {
        chartHelper.seriesDataset.clear();

        XYSeries seriesBloodSugar = new XYSeries(NORMAL);
        chartHelper.seriesDataset.addSeries(seriesBloodSugar);

        XYSeries seriesBloodSugarHyper = new XYSeries(HYPERGLYCEMIA);
        XYSeries seriesBloodSugarHypo = new XYSeries(HYPOGLYCEMIA);

        if(preferenceHelper.limitsAreHighlighted()) {
            chartHelper.seriesDataset.addSeries(seriesBloodSugarHyper);
            chartHelper.seriesDataset.addSeries(seriesBloodSugarHypo);
        }

        DatabaseDataSource dataSource = new DatabaseDataSource(getActivity());
        dataSource.open();
        List<Event> bloodSugar = dataSource.getEventsOfDay(Calendar.getInstance(), Event.Category.BloodSugar);
        dataSource.close();

        for(Event event : bloodSugar) {
            float x_value = Helper.formatCalendarToHourMinutes(event.getDate());

            float y_value = preferenceHelper.
                    formatDefaultToCustomUnit(Event.Category.BloodSugar, event.getValue());

            // Adjust y axis
            if(y_value > chartHelper.renderer.getYAxisMax())
                chartHelper.renderer.setYAxisMax(preferenceHelper.
                        formatDefaultToCustomUnit(Event.Category.BloodSugar, y_value + 30));

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
        chartHelper.initialize();
        linearLayoutChart.removeAllViews();
        linearLayoutChart.addView(chartHelper.chartView);
        chartHelper.chartView.repaint();
    }

    private class ChartTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... urls) {
            updateChart();
            return null;
        }

        protected void onPostExecute(Void param) {
            initializeChart();
        }
    }

    // endregion
}
