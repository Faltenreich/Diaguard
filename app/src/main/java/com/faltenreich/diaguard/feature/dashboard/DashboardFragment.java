package com.faltenreich.diaguard.feature.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentDashboardBinding;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditIntentFactory;
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

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding> implements ToolbarDescribing, MainButton {

    private Entry latestEntry;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    protected FragmentDashboardBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentDashboardBinding.inflate(layoutInflater);
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
        initLayout();
        initializeChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateContent();
    }

    private void initLayout() {
        getBinding().latestLayout.setOnClickListener((view) -> openEntry());
        getBinding().todayLayout.setOnClickListener((view) -> openStatistics());
        getBinding().averageLayout.setOnClickListener((view) -> openStatistics());
        getBinding().trendLayout.setOnClickListener((view) -> openStatistics());
        getBinding().hba1cLayout.setOnClickListener((view) -> showHbA1cFormula());
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

            getBinding().alarmLayout.alarmLayout.setVisibility(View.VISIBLE);
            getBinding().alarmLayout.alarmLabel.setText(String.format("%s %s",
                    getString(R.string.alarm_reminder_in),
                    DateTimeUtils.parseInterval(getContext(), alarmIntervalInMillis)));

            getBinding().alarmLayout.alarmDeleteButton.setOnClickListener(v -> {
                AlarmUtils.stopAlarm();
                updateReminder();

                ViewUtils.showSnackbar(getView(), getString(R.string.alarm_deleted), v1 -> {
                    AlarmUtils.setAlarm(alarmIntervalInMillis);
                    updateReminder();
                });
            });
        } else {
            getBinding().alarmLayout.alarmLayout.setVisibility(View.GONE);
        }
    }

    private void updateLatest() {
        if (getContext() != null) {
            TextView latestValueLabel = getBinding().latestValueLabel;
            TextView latestTimeLabel = getBinding().latestTimeLabel;
            TextView latestAgoLabel = getBinding().latestAgoLabel;
            if (latestEntry != null) {
                latestValueLabel.setTextSize(54);
                BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(latestEntry);

                // Value
                latestValueLabel.setText(bloodSugar.toString());

                // Highlighting
                if (PreferenceStore.getInstance().limitsAreHighlighted()) {
                    if (bloodSugar.getMgDl() > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                        latestValueLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    } else if (bloodSugar.getMgDl() < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                        latestValueLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                    } else {
                        latestValueLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    }
                }

                // Time
                latestTimeLabel.setText(String.format("%s %s - ",
                        Helper.getDateFormat().print(latestEntry.getDate()),
                        Helper.getTimeFormat().print(latestEntry.getDate())));
                int differenceInMinutes = Minutes.minutesBetween(latestEntry.getDate(), new DateTime()).getMinutes();

                // Highlight if last measurement is more than eight hours ago
                latestAgoLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                if (differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8) {
                    latestAgoLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                }

                latestAgoLabel.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
            } else {
                latestValueLabel.setTextSize(32);
                latestValueLabel.setText(R.string.first_visit);
                latestValueLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

                latestTimeLabel.setText(R.string.first_visit_desc);
                latestAgoLabel.setText(null);
                latestAgoLabel.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_darker));
            }
        }
    }

    private void updateDashboard() {
        new DashboardTask(getContext(), values -> {
            if (isAdded() && values != null && values.length == 7) {
                getBinding().totalCountLabel.setText(values[0]);
                getBinding().hyperglycemiaCountLabel.setText(values[1]);
                getBinding().hypoglycemiaCountLabel.setText(values[2]);
                getBinding().averageDayLabel.setText(values[3]);
                getBinding().averageWeekLabel.setText(values[4]);
                getBinding().averageMonthLabel.setText(values[5]);
                getBinding().hba1cLabel.setText(values[6]);
            }
        }).execute();
    }

    private void initializeChart() {
        LineChart chartView = getBinding().chartView;
        TimeSpan timeSpan = TimeSpan.WEEK;

        ChartUtils.setChartDefaultStyle(chartView, Category.BLOODSUGAR);

        chartView.setTouchEnabled(false);
        chartView.getAxisLeft().setDrawAxisLine(false);
        chartView.getAxisLeft().setDrawGridLines(false);
        chartView.getAxisLeft().setDrawLabels(false);
        chartView.getXAxis().setDrawGridLines(false);
        chartView.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
        chartView.getAxisLeft().removeAllLimitLines();
        float targetValue = PreferenceStore.getInstance().
                formatDefaultToCustomUnit(Category.BLOODSUGAR,
                        PreferenceStore.getInstance().getTargetValue());
        chartView.getAxisLeft().addLimitLine(ChartUtils.getLimitLine(getContext(), targetValue, R.color.gray_light));
        chartView.getXAxis().setValueFormatter((value, axis) -> {
            int daysPast = -(timeSpan.stepsPerInterval - (int) value);
            DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
            return timeSpan.getLabel(dateTime);
        });
        chartView.getXAxis().setAxisMaximum(timeSpan.stepsPerInterval);
    }

    private void updateChart() {
        LineChart chartView = getBinding().chartView;
        new MeasurementAverageTask(getContext(), Category.BLOODSUGAR, TimeSpan.WEEK, true, false, lineData -> {
            if (isAdded()) {
                chartView.clear();
                if (lineData != null) {
                    chartView.setData(lineData);
                }
                chartView.invalidate();
            }
        }).execute();
    }

    private void openStatistics() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openFragment(R.id.nav_statistics);
        }
    }

    private void openEntry() {
        Intent intent = EntryEditIntentFactory.newInstance(getContext(), latestEntry);
        startActivity(intent);
    }

    private void showHbA1cFormula() {
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
                Intent intent = EntryEditIntentFactory.newInstance(getContext());
                startActivity(intent);
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
