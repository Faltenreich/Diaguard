package com.faltenreich.diaguard.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.ChartHelper;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.sql.SQLException;
import java.util.List;

public class MainFragment extends BaseFragment {

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

        try {
            long countBloodSugarMeasurements = DatabaseFacade.getInstance().getDao(BloodSugar.class).countOf();

            if(countBloodSugarMeasurements > 0) {
                layoutLatest.setOnClickListener(null);
                textViewLatestValue.setTextSize(60);
                updateLatest();
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
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
        }

        updateChart();
    }

    private void updateLatest() throws SQLException{
        // TODO: SELECT * FROM join-statement instead of two queries
        QueryBuilder joinQb = DatabaseFacade.getInstance().join(Entry.class, BloodSugar.class).orderBy(Entry.DATE, false).limit(1L);
        Entry entry = (Entry) joinQb.query().get(0);
        Where where = DatabaseFacade.getInstance().getDao(BloodSugar.class).queryBuilder().where().eq(BloodSugar.ENTRY_ID, entry.getId());
        BloodSugar latestBloodSugar = (BloodSugar) where.query().get(0);

        // Value
        float value = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar, latestBloodSugar.getMgDl());
        textViewLatestValue.setText(PreferenceHelper.getInstance().
                getDecimalFormat(Measurement.Category.BloodSugar).format(value));

        // Highlighting
        if(PreferenceHelper.getInstance().limitsAreHighlighted()) {
            if(latestBloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.red));
            else if(latestBloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia())
                textViewLatestValue.setTextColor(getResources().getColor(R.color.blue));
            else
                textViewLatestValue.setTextColor(getResources().getColor(R.color.green));
        }

        // Unit
        textViewLatestUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BloodSugar));

        // Time
        textViewLatestTime.setText(PreferenceHelper.getInstance().
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
        try {
            QueryBuilder<Entry, ?> jointQuery = DatabaseFacade.getInstance().join(Entry.class, BloodSugar.class);
            Where<Entry, ?> whereToday = jointQuery.where().gt(Entry.DATE, DateTime.now().withTimeAtStartOfDay());
            List<Entry> entriesWithBloodSugar = whereToday.query();
            textViewMeasurements.setText(Long.toString(entriesWithBloodSugar.size()));

            int countHypers = 0;
            int countHypos = 0;
            for (Entry entry : entriesWithBloodSugar) {
                List<BloodSugar> bloodSugarList = DatabaseFacade.getInstance().getDao(BloodSugar.class).queryBuilder().where().eq(Measurement.ENTRY_ID, entry.getId()).query();
                for (BloodSugar bloodSugar : bloodSugarList) {
                    float mgDl = bloodSugar.getMgDl();
                    if (mgDl > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        countHypers++;
                    } else if (mgDl < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        countHypos++;
                    }
                }
            }
            textViewHyperglycemia.setText(countHypers > 0 ? Long.toString(countHypers) : Helper.PLACEHOLDER);
            textViewHypoglycemia.setText(countHypos > 0 ? Long.toString(countHypos) : Helper.PLACEHOLDER);
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
        }
    }

    private void updateAverage() {
        try {
            DateTime now = DateTime.now();
            Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
            Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);

            float avgDay = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, now);
            float avgWeek = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, intervalWeek);
            float avgMonth = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, intervalMonth);

            if (avgDay > 0) {
                float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgDay);
                textViewAverageDay.setText(PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgDayCustom));
            } else {
                textViewAverageDay.setText(Helper.PLACEHOLDER);
            }

            if (avgWeek > 0) {
                float avgDayWeek = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgWeek);
                textViewAverageWeek.setText(PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgDayWeek));
            } else {
                textViewAverageWeek.setText(Helper.PLACEHOLDER);
            }

            if (avgMonth > 0) {
                float avgDayMonth = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgMonth);
                textViewAverageMonth.setText(PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgDayMonth));
            } else {
                textViewAverageMonth.setText(Helper.PLACEHOLDER);
            }
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
        }
    }

    private void updateChart() {
        ChartHelper chartHelper = new ChartHelper(getActivity(), ChartHelper.ChartType.LineChart, ChartHelper.Interval.Week);
        chartHelper.renderer.removeAllRenderers();
        chartHelper.seriesDataset.clear();
        chartHelper.render();

        XYSeriesRenderer seriesRenderer = ChartHelper.getSeriesRendererForBloodSugar(getActivity());
        seriesRenderer.setColor(getResources().getColor(R.color.green_light));
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

        float highestValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        PreferenceHelper.getInstance().getLimitHyperglycemia());
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
            try {
                float averageOfDay = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, day);
                if(averageOfDay > 0) {
                    float y_value = PreferenceHelper.getInstance().
                            formatDefaultToCustomUnit(Measurement.Category.BloodSugar, averageOfDay);
                    // Adjust y axis
                    if(y_value > highestValue) {
                        highestValue = y_value;
                    }
                    seriesBloodSugar.add(x_value, y_value);
                    count++;
                }
            } catch (SQLException exception) {
                Log.e("avg(DateTime)", exception.getMessage());
            }
        }
        chartHelper.renderer.setYAxisMax(highestValue +
                PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, 30));
        if(count >= 2) {
            seriesRenderer.setPointStyle(PointStyle.POINT);
        }

        // Orientation lines
        chartHelper.renderer.setGridColor(getResources().getColor(R.color.green_light));
        chartHelper.renderer.setShowCustomTextGridY(true);
        chartHelper.renderer.setShowGridX(true);
        chartHelper.renderer.setYLabels(0);
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        PreferenceHelper.getInstance().getTargetValue());
        chartHelper.renderer.addYTextLabel(targetValue, "1");
        chartHelper.renderer.setYAxisMin(0);

        chartHelper.initialize();
        layoutChart.removeAllViews();
        layoutChart.addView(chartHelper.chartView);
        chartHelper.chartView.repaint();
    }
}
