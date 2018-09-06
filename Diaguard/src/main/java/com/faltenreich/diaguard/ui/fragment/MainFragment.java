package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.event.preference.UnitChangedEvent;
import com.faltenreich.diaguard.ui.activity.EntryActivity;
import com.faltenreich.diaguard.ui.activity.MainActivity;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;
import com.faltenreich.diaguard.util.AlarmUtils;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.DateTimeUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.DashboardTask;
import com.faltenreich.diaguard.util.thread.MeasurementAverageTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Minutes;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements MainButton {

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

    public MainFragment() {
        super(R.layout.fragment_main, R.string.app_name, R.menu.main);
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
                    DateTimeUtils.parseInterval(alarmIntervalInMillis)));

            buttonAlarmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmUtils.stopAlarm();
                    updateReminder();

                    ViewUtils.showSnackbar(getView(), getString(R.string.alarm_deleted), new View.OnClickListener() {
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
            textViewLatestValue.setTextSize(54);
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

            // Time
            textViewLatestTime.setText(String.format("%s %s - ",
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
            textViewLatestValue.setTextSize(32);
            textViewLatestValue.setText(R.string.first_visit);
            textViewLatestValue.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            textViewLatestTime.setText(R.string.first_visit_desc);
            textViewLatestAgo.setText(null);
            textViewLatestAgo.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
        }
    }

    private void updateDashboard() {
        new DashboardTask(getContext(), new BaseAsyncTask.OnAsyncProgressListener<String[]>() {
            @Override
            public void onPostExecute(String[] values) {
                if (isAdded() && values != null && values.length == 7) {
                    textViewMeasurements.setText(values[0]);
                    textViewHyperglycemia.setText(values[1]);
                    textViewHypoglycemia.setText(values[2]);
                    textViewAverageDay.setText(values[3]);
                    textViewAverageWeek.setText(values[4]);
                    textViewAverageMonth.setText(values[5]);
                    textViewHbA1c.setText(values[6]);
                }
            }
        }).execute();
    }

    private void initializeChart() {
        final TimeSpan timeSpan = TimeSpan.WEEK;
        ChartHelper.setChartDefaultStyle(chart, Measurement.Category.BLOODSUGAR);
        chart.setTouchEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
        chart.getAxisLeft().removeAllLimitLines();
        float targetValue = PreferenceHelper.getInstance().
                formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                        PreferenceHelper.getInstance().getTargetValue());
        chart.getAxisLeft().addLimitLine(ChartHelper.getLimitLine(getContext(), targetValue, R.color.gray_light));
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int daysPast = -(timeSpan.stepsPerInterval - (int) value);
                DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
                return timeSpan.getLabel(dateTime);
            }
        });
        chart.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
    }

    private void updateChart() {
        new MeasurementAverageTask(getContext(), Measurement.Category.BLOODSUGAR, TimeSpan.WEEK, true, false, new BaseAsyncTask.OnAsyncProgressListener<LineData>() {
            @Override
            public void onPostExecute(LineData lineData) {
                if (isAdded()) {
                    chart.clear();
                    if (lineData != null) {
                        chart.setData(lineData);
                    }
                    chart.invalidate();
                }
            }
        }).execute();
    }

    private void openStatistics() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(R.id.nav_statistics);
        }
    }

    @OnClick(R.id.layout_latest)
    protected void openEntry(View view) {
        EntryActivity.show(getContext(), view, latestEntry);
    }

    @OnClick(R.id.layout_today)
    protected void openStatisticsToday() {
        openStatistics();
    }

    @OnClick(R.id.layout_average)
    protected void openStatisticsAverage() {
        openStatistics();
    }

    @OnClick(R.id.layout_trend)
    protected void openTrend() {
        openStatistics();
    }

    @OnClick(R.id.layout_hba1c)
    protected void showHbA1cFormula() {
        String formula = String.format(getString(R.string.hba1c_formula),
                getString(R.string.average_symbol),
                getString(R.string.months),
                getString(R.string.bloodsugar));
        ViewUtils.showSnackbar(getView(), formula);
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() != null) {
                    EntryActivity.show(getContext());
                }
            }
        });
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
