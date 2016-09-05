package com.faltenreich.diaguard.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.SqlFunction;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.StatisticsActivity;
import com.faltenreich.diaguard.util.AlarmUtils;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.ViewHelper;
import com.faltenreich.diaguard.util.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.UpdateLineChartTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment {

    @BindView(R.id.chart) LineChart chart;
    @BindView(R.id.layout_alarm) ViewGroup layoutAlarm;
    @BindView(R.id.alarm_text) TextView textViewAlarm;
    @BindView(R.id.alarm_delete) View buttonAlarmDelete;
    @BindView(R.id.textview_latest_value) TextView textViewLatestValue;
    @BindView(R.id.textview_latest_unit) TextView textViewLatestUnit;
    @BindView(R.id.textview_latest_time) TextView textViewLatestTime;
    @BindView(R.id.textview_latest_ago) TextView textViewLatestAgo;
    @BindView(R.id.textview_measurements) TextView textViewMeasurements;
    @BindView(R.id.textview_hyperglycemia) TextView textViewHyperglycemia;
    @BindView(R.id.textview_hypoglycemia) TextView textViewHypoglycemia;
    @BindView(R.id.textview_avg_month) TextView textViewAverageMonth;
    @BindView(R.id.textview_avg_week) TextView textViewAverageWeek;
    @BindView(R.id.textview_avg_day) TextView textViewAverageDay;
    @BindView(R.id.hba1c_value) TextView textViewHbA1c;

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
        return DiaguardApplication.getContext().getString(R.string.app_name);
    }

    private void updateContent() {
        latestEntry = EntryDao.getInstance().getLatestWithMeasurement(BloodSugar.class);
        updateReminder();
        updateLatest();
        updateDashboard();
        updateChart();
    }

    private void updateReminder() {
        if (AlarmUtils.isAlarmSet()) {
            final long alarmIntervalInMillis = AlarmUtils.getAlarmInMillis() - DateTime.now().getMillis();

            layoutAlarm.setVisibility(View.VISIBLE);
            textViewAlarm.setText(String.format("%s %s",
                    getString(R.string.alarm_reminder),
                    DateTimeUtils.parseInterval(alarmIntervalInMillis)));

            buttonAlarmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmUtils.stopAlarm();
                    updateReminder();

                    ViewHelper.showSnackbar(getView(), getString(R.string.alarm_deleted), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlarmUtils.setAlarm(alarmIntervalInMillis);
                            updateReminder();
                        }
                    });
                }
            });
        } else {
            layoutAlarm.setVisibility(View.GONE);
        }
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
            textViewLatestValue.setText(R.string.first_visit);
            textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            textViewLatestUnit.setText(null);

            textViewLatestAgo.setText(R.string.first_visit_desc);
            textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
            textViewLatestTime.setText(null);
        }
    }

    // region Dashboard

    private void updateDashboard() {
        new UpdateDashboardTask().execute();
    }

    private class UpdateDashboardTask extends AsyncTask<Void, Void, String[]> {

        protected String[] doInBackground(Void... params) {
            int countHypers = 0;
            int countHypos = 0;

            List<Entry> entriesWithBloodSugar = EntryDao.getInstance().getAllWithMeasurementFromToday(BloodSugar.class);
            if (entriesWithBloodSugar != null) {
                for (Entry entry : entriesWithBloodSugar) {
                    BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(entry);
                    float mgDl = bloodSugar.getMgDl();
                    if (mgDl > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                        countHypers++;
                    } else if (mgDl < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                        countHypos++;
                    }
                }
            }
            DateTime now = DateTime.now();
            Interval intervalDay = new Interval(now, now);
            Interval intervalWeek = new Interval(new DateTime(now.minusWeeks(1)), now);
            Interval intervalMonth = new Interval(new DateTime(now.minusMonths(1)), now);
            Interval intervalQuarter = new Interval(new DateTime(now.minusMonths(3)), now);

            float avgDay = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalDay);
            float avgDayCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgDay);

            float avgWeek = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalWeek);
            float avgWeekCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgWeek);

            float avgMonth = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalMonth);
            float avgMonthCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, avgMonth);

            float avgQuarter = MeasurementDao.getInstance(BloodSugar.class).function(SqlFunction.AVG, BloodSugar.Column.MGDL, intervalQuarter);
            float hbA1cCustom = 0;
            if (avgQuarter > 0) {
                float hbA1c = Helper.calculateHbA1c(avgQuarter);
                hbA1cCustom = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.HBA1C, hbA1c);
            }

            return new String[] {
                    Integer.toString(entriesWithBloodSugar != null ? entriesWithBloodSugar.size() : 0),
                    Integer.toString(countHypers),
                    Integer.toString(countHypos),
                    avgDayCustom > 0 ? Helper.parseFloat(avgDayCustom) : getContext().getString(R.string.placeholder),
                    avgWeekCustom > 0 ? Helper.parseFloat(avgWeekCustom) : getContext().getString(R.string.placeholder),
                    avgMonthCustom > 0 ? Helper.parseFloat(avgMonthCustom) : getContext().getString(R.string.placeholder),
                    hbA1cCustom > 0 ? String.format("%s%s", Helper.parseFloat(hbA1cCustom), PreferenceHelper.getInstance().getUnitAcronym(Measurement.Category.HBA1C)) : getContext().getString(R.string.placeholder)
            };
        }

        protected void onPostExecute(String[] values) {
            if(isAdded()) {
                if (values.length == 7) {
                    // Today
                    textViewMeasurements.setText(values[0]);
                    textViewHyperglycemia.setText(values[1]);
                    textViewHypoglycemia.setText(values[2]);

                    // Averages
                    textViewAverageDay.setText(values[3]);
                    textViewAverageWeek.setText(values[4]);
                    textViewAverageMonth.setText(values[5]);

                    // HbA1c
                    textViewHbA1c.setText(values[6]);
                }
            }
        }
    }

    // endregion

    // region Charting

    private void initializeChart() {
        ChartHelper.setChartDefaultStyle(chart, Measurement.Category.BLOODSUGAR);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
        chart.getXAxis().setLabelsToSkip(0);
        chart.getAxisLeft().removeAllLimitLines();
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                        PreferenceHelper.getInstance().getTargetValue());
        chart.getAxisLeft().addLimitLine(ChartHelper.getLimitLine(getContext(), targetValue, R.color.gray_light));
    }

    private void updateChart() {
        new UpdateLineChartTask(getContext(), new BaseAsyncTask.OnAsyncProgressListener<LineData>() {
            @Override
            public void onPostExecute(LineData lineData) {
                if(isAdded()) {
                    initializeChart();
                    chart.setData(lineData);
                    chart.invalidate();
                }
            }
        }, Measurement.Category.BLOODSUGAR, TimeSpan.WEEK, true, false).execute();
    }

    private void openStatistics(View view, String transitionName) {
        Intent intent = new Intent(getActivity(), StatisticsActivity.class);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        transitionName);
        startActivity(intent, options);
    }

    // endregion

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_latest)
    protected void openEntry(View view) {
        Intent intent = new Intent(getActivity(), EntryActivity.class);
        if (latestEntry != null) {
            intent.putExtra(EntryActivity.EXTRA_ENTRY, latestEntry.getId());
        }
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view,
                        "transitionEntry");
        startActivity(intent, options);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_today)
    protected void openStatisticsToday(View view) {
        startActivity(new Intent(getActivity(), StatisticsActivity.class));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_average)
    protected void openStatisticsAverage(View view) {
        openStatistics(view, "transitionOverview");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_trend)
    protected void openTrend(View view) {
        openStatistics(view, "transitionTrend");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.layout_hba1c)
    protected void showHbA1cFormula(View view) {
        String formula = String.format(getString(R.string.hba1c_formula),
                getString(R.string.average_symbol),
                getString(R.string.months),
                getString(R.string.bloodsugar));
        ViewHelper.showSnackbar(getView(), formula);
    }

    @SuppressWarnings("unused")
    public void onEvent(EntryAddedEvent event) {
        updateContent();
    }
}
