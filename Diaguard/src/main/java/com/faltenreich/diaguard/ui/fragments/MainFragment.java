package com.faltenreich.diaguard.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.ui.activity.BaseActivity;
import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.ui.activity.EntryDetailActivity;
import com.faltenreich.diaguard.ui.activity.NewEventActivity;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.DatabaseHelper;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainFragment extends BaseFragment {

    @Bind(R.id.chart)
    protected LineChart chart;

    @Bind(R.id.textview_latest_value)
    protected TextView textViewLatestValue;

    @Bind(R.id.textview_latest_unit)
    protected TextView textViewLatestUnit;

    @Bind(R.id.textview_latest_time)
    protected TextView textViewLatestTime;

    @Bind(R.id.textview_latest_ago)
    protected TextView textViewLatestAgo;

    @Bind(R.id.textview_measurements)
    protected TextView textViewMeasurements;

    @Bind(R.id.textview_hyperglycemia)
    protected TextView textViewHyperglycemia;

    @Bind(R.id.textview_hypoglycemia)
    protected TextView textViewHypoglycemia;

    @Bind(R.id.textview_avg_month)
    protected TextView textViewAverageMonth;

    @Bind(R.id.textview_avg_week)
    protected TextView textViewAverageWeek;

    @Bind(R.id.textview_avg_day)
    protected TextView textViewAverageDay;

    private Entry latestEntry;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initializeChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    @Override
    public String getTitle() {
        return DiaguardApplication.getContext().getString(R.string.home);
    }

    private void updateContent() {
        latestEntry = EntryDao.getInstance().getLatestWithMeasurement(BloodSugar.class);
        updateLatest();
        updateDashboard();
        updateChart();
    }

    private void updateLatest() {
        if (latestEntry != null) {
            textViewLatestValue.setTextSize(60);
            BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(latestEntry);

            // Value
            textViewLatestValue.setText(bloodSugar.toString());

            // Highlighting
            if(PreferenceHelper.getInstance().limitsAreHighlighted()) {
                if(bloodSugar.getMgDl() > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                } else if(bloodSugar.getMgDl() < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                } else {
                    textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                }
            }

            // Unit
            textViewLatestUnit.setText(PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.BLOODSUGAR));

            // Time
            textViewLatestTime.setText(String.format("%s %s | ",
                    Helper.getDateFormat().print(latestEntry.getDate()),
                    Helper.getTimeFormat().print(latestEntry.getDate())));
            int differenceInMinutes = Minutes.minutesBetween(latestEntry.getDate(), new DateTime()).getMinutes();

            // Highlight if last measurement is more than eight hours ago
            textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            if(differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8) {
                textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }

            textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
        } else {
            textViewLatestValue.setTextSize(40);
        }
    }

    // region Dashboard

    private void updateDashboard() {
        new UpdateDashboardTask().execute();
    }

    private class UpdateDashboardTask extends AsyncTask<Void, Void, String[]> {

        protected String[] doInBackground(Void... params) {
            List<Entry> entriesWithBloodSugar = EntryDao.getInstance().getAllWithMeasurementFromToday(BloodSugar.class);

            int countHypers = 0;
            int countHypos = 0;
            for (Entry entry : entriesWithBloodSugar) {
                BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(entry);
                float mgDl = bloodSugar.getMgDl();
                if (mgDl > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                    countHypers++;
                } else if (mgDl < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                    countHypos++;
                }
            }
            DateTime now = DateTime.now();
            Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
            Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);

            float avgDay = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, now);
            float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgDay);

            float avgWeek = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, intervalWeek);
            float avgWeekCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgWeek);

            float avgMonth = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, intervalMonth);
            float avgMonthCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgMonth);

            return new String[] {
                    Integer.toString(entriesWithBloodSugar.size()),
                    Integer.toString(countHypers),
                    Integer.toString(countHypos),
                    PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(avgDayCustom),
                    PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(avgWeekCustom),
                    PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(avgMonthCustom)
            };
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
        chart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
        chart.getXAxis().setLabelsToSkip(0);
        float offset = Helper.getDPI(20);
        chart.setViewPortOffsets(offset, 0, offset, offset * 1.2f);
        chart.getAxisLeft().addLimitLine(getLimitLine());
    }

    private LimitLine getLimitLine() {
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                        PreferenceHelper.getInstance().getTargetValue());
        LimitLine limitLine = new LimitLine(targetValue, getString(R.string.hyper));
        limitLine.setLineColor(ContextCompat.getColor(getContext(), R.color.gray_light));
        limitLine.setLabel(null);
        return limitLine;
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
                    formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                            PreferenceHelper.getInstance().getTargetValue());
            float highestValue = targetValue * 2;
            while (!currentDay.isAfter(today)) {
                int index = DateTimeConstants.DAYS_PER_WEEK - Days.daysBetween(currentDay, today).getDays() - 1;
                xLabels.add(index, weekDays[currentDay.dayOfWeek().get() - 1]);
                float avg = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, currentDay);
                if (avg > 0) {
                    entries.add(new com.github.mikephil.charting.data.Entry(avg, index));
                    if (avg > highestValue) {
                        highestValue = avg;
                    }
                }
                currentDay = currentDay.plusDays(1);
            }
            xLabels.set(xLabels.size() - 1, getString(R.string.today));

            ArrayList<LineDataSet> dataSets = new ArrayList<>();
            LineDataSet dataSet = new LineDataSet(entries, BloodSugar.class.getSimpleName());
            int dataSetColor = ContextCompat.getColor(getContext(), R.color.green_light);
            dataSet.setColor(dataSetColor);
            dataSet.setCircleColor(dataSetColor);
            dataSet.setCircleSize(ChartHelper.CIRCLE_SIZE);
            dataSet.setDrawCircles(entries.size() <= 1);
            dataSet.setDrawValues(false);
            dataSet.setLineWidth(Helper.getDPI(ChartHelper.LINE_WIDTH));
            dataSets.add(dataSet);

            // Workaround to set visible area
            List<com.github.mikephil.charting.data.Entry> entriesMaximum = new ArrayList<>();
            entriesMaximum.add(new com.github.mikephil.charting.data.Entry(highestValue, xLabels.size()));
            LineDataSet dataSetMaximum = new LineDataSet(entriesMaximum, "Maximum");
            dataSetMaximum.setColor(Color.TRANSPARENT);
            dataSets.add(dataSetMaximum);

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

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_latest)
    protected void openEntry() {
        if (latestEntry != null) {
            Intent intent = new Intent(getActivity(), EntryDetailActivity.class);
            intent.putExtra(EntryDetailFragment.EXTRA_ENTRY, latestEntry.getId());
            startActivity(intent);
        } else {
            startActivity(new Intent(getActivity(), NewEventActivity.class));
        }
    }
}
