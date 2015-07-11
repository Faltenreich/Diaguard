package com.faltenreich.diaguard.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.BaseActivity;
import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.MainActivity;
import com.faltenreich.diaguard.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.StatisticsActivity;
import com.faltenreich.diaguard.database.DatabaseFacade;
import com.faltenreich.diaguard.database.DatabaseHelper;
import com.faltenreich.diaguard.database.Entry;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.ChartHelper;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {

    private ViewGroup layoutLatest;
    private ViewGroup layoutToday;
    private ViewGroup layoutAverage;
    private ViewGroup layoutTrend;
    private LineChart chart;

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
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        updateContent();
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.home);
    }

    @Override
    public boolean hasAction() {
        return false;
    }

    @Override
    public void action(View view) {}

    private void getComponents() {
        layoutLatest = (ViewGroup) getView().findViewById(R.id.layout_latest);
        layoutToday = (ViewGroup) getView().findViewById(R.id.layout_today);
        layoutAverage = (ViewGroup) getView().findViewById(R.id.layout_average);
        layoutTrend = (ViewGroup) getView().findViewById(R.id.layout_trend);
        chart = (LineChart) getView().findViewById(R.id.chart);

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

    private void initialize() {
        layoutToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatisticsActivity.class);
                intent.putExtra(StatisticsActivity.EXTRA_TAB, 0);
                startActivity(intent);
            }
        });
        layoutAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatisticsActivity.class);
                intent.putExtra(StatisticsActivity.EXTRA_TAB, 0);
                startActivity(intent);
            }
        });
        layoutTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StatisticsActivity.class);
                intent.putExtra(StatisticsActivity.EXTRA_TAB, 1);
                startActivity(intent);
            }
        });
        initializeChart();
    }

    private void updateContent() {
        updateLatest();
        updateDashboard();
        updateChart();
    }

    private void updateLatest() {
        try {
            QueryBuilder<Entry, ?> joinQb = DatabaseFacade.getInstance().join(Entry.class, BloodSugar.class).orderBy(Entry.DATE, false).limit(1L);
            List<Entry> entries = joinQb.query();
            if (entries.size() > 0) {
                textViewLatestValue.setTextSize(60);

                final Entry entry = entries.get(0);
                Where<BloodSugar, ?> where = DatabaseFacade.getInstance().getDao(BloodSugar.class).queryBuilder().where().eq(BloodSugar.ENTRY_ID, entry.getId());
                BloodSugar bloodSugar = where.query().get(0);

                // Value
                textViewLatestValue.setText(PreferenceHelper.getInstance()
                        .getDecimalFormat(Measurement.Category.BloodSugar)
                        .format(bloodSugar.getValueForUser()));

                // Highlighting
                if(PreferenceHelper.getInstance().limitsAreHighlighted()) {
                    if(bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        textViewLatestValue.setTextColor(getResources().getColor(R.color.red));
                    } else if(bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        textViewLatestValue.setTextColor(getResources().getColor(R.color.blue));
                    } else {
                        textViewLatestValue.setTextColor(getResources().getColor(R.color.green));
                    }
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
                if(differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8) {
                    textViewLatestAgo.setTextColor(getResources().getColor(R.color.red));
                }

                textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));

                layoutLatest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewEventActivity.class);
                        intent.putExtra(EntryDetailFragment.ENTRY_ID, entry.getId());
                        startActivityForResult(intent, MainActivity.REQUEST_EVENT_CREATED);
                    }
                });
            } else {
                textViewLatestValue.setTextSize(40);

                layoutLatest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(getActivity(), NewEventActivity.class), MainActivity.REQUEST_EVENT_CREATED);
                    }
                });
            }
        } catch (SQLException exception) {
            Log.e("MainFragment", exception.getMessage());
        }
    }

    // region Dashboard

    private void updateDashboard() {
        new UpdateDashboardTask().execute();
    }

    private class UpdateDashboardTask extends AsyncTask<Void, Void, String[]> {

        protected String[] doInBackground(Void... params) {
            try {
                QueryBuilder<Entry, ?> jointQuery = DatabaseFacade.getInstance().join(Entry.class, BloodSugar.class);
                Where<Entry, ?> whereToday = jointQuery.where().gt(Entry.DATE, DateTime.now().withTimeAtStartOfDay());
                List<Entry> entriesWithBloodSugar = whereToday.query();

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
                DateTime now = DateTime.now();
                Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
                Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);

                float avgDay = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, now);
                float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgDay);

                float avgWeek = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, intervalWeek);
                float avgWeekCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgWeek);

                float avgMonth = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, intervalMonth);
                float avgMonthCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, avgMonth);

                return new String[] {
                        Integer.toString(entriesWithBloodSugar.size()),
                        Integer.toString(countHypers),
                        Integer.toString(countHypos),
                        PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgDayCustom),
                        PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgWeekCustom),
                        PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(avgMonthCustom)};

            } catch (SQLException exception) {
                Log.e("MainFragment", exception.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String[] values) {
            if(isAdded()) {
                if (values.length == 6) {
                    // Today
                    textViewMeasurements.setText(values[0]);
                    textViewHyperglycemia.setText(values[1]);
                    textViewHypoglycemia.setText(values[2]);

                    // Averages
                    textViewAverageDay.setText(values[3]);
                    textViewAverageWeek.setText(values[4]);
                    textViewAverageMonth.setText(values[5]);
                }
            }
        }
    }

    // endregion

    // region Charting

    private void initializeChart() {
        ChartHelper.setChartDefaultStyle(chart);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(getResources().getColor(R.color.gray_dark));
        chart.getXAxis().setLabelsToSkip(0);
        float offset = Helper.getDPI(20);
        chart.setViewPortOffsets(offset, 0, offset, offset * 1.2f);
        chart.getAxisLeft().addLimitLine(getLimitLine());
    }

    private LimitLine getLimitLine() {
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                        PreferenceHelper.getInstance().getTargetValue());
        LimitLine hyperglycemia = new LimitLine(targetValue, getString(R.string.hyper));
        hyperglycemia.setLineColor(getResources().getColor(R.color.green_light));
        hyperglycemia.setLabel(null);
        return hyperglycemia;
    }

    private void updateChart() {
        new UpdateChartTask().execute();
    }

    private class UpdateChartTask extends AsyncTask<Void, Void, LineData> {

        protected LineData doInBackground(Void... params) {
            DateTime today = DateTime.now();
            DateTime currentDay = today.minusWeeks(1).plusDays(1);

            List<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();
            ArrayList<String> xLabels = new ArrayList<>();
            String[] weekDays = getResources().getStringArray(R.array.weekdays_short);

            float targetValue = PreferenceHelper.getInstance().
                    formatDefaultToCustomUnit(Measurement.Category.BloodSugar,
                            PreferenceHelper.getInstance().getTargetValue());
            float highestValue = targetValue * 2;
            while (!currentDay.isAfter(today)) {
                int index = DateTimeConstants.DAYS_PER_WEEK - Days.daysBetween(currentDay, today).getDays() - 1;
                xLabels.add(index, weekDays[currentDay.dayOfWeek().get() - 1]);
                try {
                    float avg = DatabaseFacade.getInstance().avg(BloodSugar.class, BloodSugar.MGDL, currentDay);
                    if (avg > 0) {
                        entries.add(new com.github.mikephil.charting.data.Entry(avg, index));
                        if (avg > highestValue) {
                            highestValue = avg;
                        }
                    }
                } catch (SQLException exception) {
                    Log.e("MainFragment", exception.getMessage());
                }
                currentDay = currentDay.plusDays(1);
            }
            xLabels.set(xLabels.size() - 1, getString(R.string.today));

            ArrayList<LineDataSet> dataSets = new ArrayList<>();
            LineDataSet dataSet = new LineDataSet(entries, DatabaseHelper.BLOODSUGAR);
            int dataSetColor = getResources().getColor(R.color.green_light);
            dataSet.setColor(dataSetColor);
            dataSet.setCircleColor(dataSetColor);
            dataSet.setCircleSize(Helper.getDPI(ChartHelper.CIRCLE_SIZE));
            dataSet.setDrawValues(false);
            dataSet.setLineWidth(Helper.getDPI(ChartHelper.LINE_WIDTH));
            dataSets.add(dataSet);

            // Workaround to set visible area
            if (entries.size() > 0) {
                List<com.github.mikephil.charting.data.Entry> entriesMaximum = new ArrayList<>();
                entriesMaximum.add(new com.github.mikephil.charting.data.Entry(highestValue, xLabels.size()));
                LineDataSet dataSetMaximum = new LineDataSet(entriesMaximum, "Maximum");
                dataSetMaximum.setColor(Color.TRANSPARENT);
                dataSets.add(dataSetMaximum);
            } else {
                xLabels = new ArrayList<>();
            }

            return new LineData(xLabels, dataSets);
        }

        protected void onPostExecute(LineData data) {
            if(isAdded()) {
                chart.setData(data);
                chart.invalidate();
            }
        }
    }

    // endregion
}
