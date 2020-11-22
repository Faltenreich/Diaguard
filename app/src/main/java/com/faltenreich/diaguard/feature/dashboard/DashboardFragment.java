package com.faltenreich.diaguard.feature.dashboard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditActivity;
import com.faltenreich.diaguard.feature.navigation.MainActivity;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.statistic.MeasurementAverageTask;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Minutes;

import butterknife.BindView;
import butterknife.OnClick;

public class DashboardFragment extends BaseFragment implements ToolbarDescribing, MainButton {

    @BindView(R.id.chart) LineChart chart;
    @BindView(R.id.layout_alarm) ViewGroup layoutAlarm;
    @BindView(R.id.alarm_text) TextView textViewAlarm;
    @BindView(R.id.alarm_delete) View buttonAlarmDelete;
    @BindView(R.id.textview_latest_value) TextView textViewLatestValue;
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

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.app_name)
            .setMenu(R.menu.dashboard)
            .build();
    }

    @Override
    public void onViewCreated (@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
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
                    getString(R.string.alarm_reminder_in),
                    DateTimeUtils.parseInterval(getContext(), alarmIntervalInMillis)));

            buttonAlarmDelete.setOnClickListener(v -> {
                AlarmUtils.stopAlarm();
                updateReminder();

                ViewUtils.showSnackbar(getView(), getString(R.string.alarm_deleted), v1 -> {
                    AlarmUtils.setAlarm(alarmIntervalInMillis);
                    updateReminder();
                });
            });
        } else {
            layoutAlarm.setVisibility(View.GONE);
        }
    }

    private void updateLatest() {
        if (getContext() != null) {
            if (latestEntry != null) {
                textViewLatestValue.setTextSize(54);
                BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(latestEntry);

                // Value
                textViewLatestValue.setText(bloodSugar.toString());

                // Highlighting
                if (PreferenceStore.getInstance().limitsAreHighlighted()) {
                    if (bloodSugar.getMgDl() > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    } else if (bloodSugar.getMgDl() < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                    } else {
                        textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    }
                }

                // Time
                textViewLatestTime.setText(String.format("%s %s - ",
                        Helper.getDateFormat().print(latestEntry.getDate()),
                        Helper.getTimeFormat().print(latestEntry.getDate())));
                int differenceInMinutes = Minutes.minutesBetween(latestEntry.getDate(), new DateTime()).getMinutes();

                // Highlight if last measurement is more than eight hours ago
                textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                if (differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8) {
                    textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }

                textViewLatestAgo.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
            } else {
                textViewLatestValue.setTextSize(32);
                textViewLatestValue.setText(R.string.first_visit);
                textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                textViewLatestTime.setText(R.string.first_visit_desc);
                textViewLatestAgo.setText(null);
                textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
            }
        }
    }

    private void updateDashboard() {
        new DashboardTask(getContext(), values -> {
            if (isAdded() && values != null && values.length == 7) {
                textViewMeasurements.setText(values[0]);
                textViewHyperglycemia.setText(values[1]);
                textViewHypoglycemia.setText(values[2]);
                textViewAverageDay.setText(values[3]);
                textViewAverageWeek.setText(values[4]);
                textViewAverageMonth.setText(values[5]);
                textViewHbA1c.setText(values[6]);
            }
        }).execute();
    }

    private void initializeChart() {
        final TimeSpan timeSpan = TimeSpan.WEEK;
        ChartUtils.setChartDefaultStyle(chart, Category.BLOODSUGAR);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
        chart.getAxisLeft().removeAllLimitLines();
        float targetValue = PreferenceStore.getInstance().
                formatDefaultToCustomUnit(Category.BLOODSUGAR,
                        PreferenceStore.getInstance().getTargetValue());
        chart.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), targetValue, R.color.gray_light));
        chart.getXAxis().setValueFormatter((value, axis) -> {
            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
            return timeSpan.getLabel(dateTime);
        });
        chart.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
    }

    private void updateChart() {
        new MeasurementAverageTask(getContext(), Category.BLOODSUGAR, TimeSpan.WEEK, true, false, lineData -> {
            if (isAdded()) {
                chart.clear();
                if (lineData != null) {
                    chart.setData(lineData);
                }
                chart.invalidate();
            }
        }).execute();
    }

    private void openStatistics() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openFragment(R.id.nav_statistics);
        }
    }

    @OnClick(R.id.layout_latest)
    void openEntry(View view) {
        EntryEditActivity.show(getContext(), latestEntry);
    }

    @OnClick(R.id.layout_today)
    void openStatisticsToday() {
        openStatistics();
    }

    @OnClick(R.id.layout_average)
    void openStatisticsAverage() {
        openStatistics();
    }

    @OnClick(R.id.layout_trend)
    void openTrend() {
        openStatistics();
    }

    @OnClick(R.id.layout_hba1c)
    void showHbA1cFormula() {
        String formula = String.format(getString(R.string.hba1c_formula),
                getString(R.string.average_symbol),
                getString(R.string.months),
                getString(R.string.bloodsugar));
        ViewUtils.showSnackbar(getView(), formula);
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(view -> {
            if (getContext() != null) {
                EntryEditActivity.show(getContext());
            }
        }, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        updateContent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        if (isAdded()) {
            updateContent();
        }
    }
}
