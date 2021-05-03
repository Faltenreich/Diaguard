package com.faltenreich.diaguard.feature.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentDashboardBinding;
import com.faltenreich.diaguard.feature.alarm.AlarmUtils;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragmentFactory;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.feature.statistic.MeasurementAverageTask;
import com.faltenreich.diaguard.feature.statistic.StatisticFragment;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.MeasurementDao;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.event.data.EntryAddedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.event.data.EntryUpdatedEvent;
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

    private ViewGroup latestLayout;
    private TextView latestValueLabel;
    private TextView latestTimeLabel;
    private TextView latestAgoLabel;

    private ViewGroup todayLayout;
    private TextView totalCountLabel;
    private TextView hyperglycemiaCountLabel;
    private TextView hypoglycemiaCountLabel;

    private ViewGroup averageLayout;
    private TextView averageDayLabel;
    private TextView averageWeekLabel;
    private TextView averageMonthLabel;

    private ViewGroup trendLayout;
    private LineChart chartView;

    private ViewGroup hba1cLayout;
    private TextView hba1cLabel;

    private ViewGroup alarmLayout;
    private TextView alarmLabel;
    private ImageView alarmDeleteButton;

    private Entry latestEntry;

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
        bindViews();
        initLayout();
        initializeChart();
        updateContent();
    }

    private void bindViews() {
        latestLayout = getBinding().latestLayout;
        latestValueLabel = getBinding().latestValueLabel;
        latestTimeLabel = getBinding().latestTimeLabel;
        latestAgoLabel = getBinding().latestAgoLabel;

        todayLayout = getBinding().todayLayout;
        totalCountLabel = getBinding().totalCountLabel;
        hyperglycemiaCountLabel = getBinding().hyperglycemiaCountLabel;
        hypoglycemiaCountLabel = getBinding().hypoglycemiaCountLabel;

        averageLayout = getBinding().averageLayout;
        averageDayLabel = getBinding().averageDayLabel;
        averageWeekLabel = getBinding().averageWeekLabel;
        averageMonthLabel = getBinding().averageMonthLabel;

        trendLayout = getBinding().trendLayout;
        chartView = getBinding().chartView;

        hba1cLayout = getBinding().hba1cLayout;
        hba1cLabel = getBinding().hba1cLabel;

        alarmLayout = getBinding().alarmLayout.alarmLayout;
        alarmLabel = getBinding().alarmLayout.alarmLabel;
        alarmDeleteButton = getBinding().alarmLayout.alarmDeleteButton;
    }

    private void initLayout() {
        latestLayout.setOnClickListener((view) -> openEntry());
        todayLayout.setOnClickListener((view) -> openStatistics());
        averageLayout.setOnClickListener((view) -> openStatistics());
        trendLayout.setOnClickListener((view) -> openStatistics());
        hba1cLayout.setOnClickListener((view) -> showHbA1cFormula());
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

            alarmLayout.setVisibility(View.VISIBLE);
            alarmLabel.setText(String.format("%s %s",
                    getString(R.string.alarm_reminder_in),
                    DateTimeUtils.parseInterval(getContext(), alarmIntervalInMillis)));

            alarmDeleteButton.setOnClickListener(v -> {
                AlarmUtils.stopAlarm();
                updateReminder();

                ViewUtils.showSnackbar(getView(), getString(R.string.alarm_deleted), v1 -> {
                    AlarmUtils.setAlarm(alarmIntervalInMillis);
                    updateReminder();
                });
            });
        } else {
            alarmLayout.setVisibility(View.GONE);
        }
    }

    private void updateLatest() {
        if (getContext() == null) {
            return;
        }
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

    private void updateDashboard() {
        new DashboardTask(getContext(), values -> {
            if (isAdded() && values != null && values.length == 7) {
                totalCountLabel.setText(values[0]);
                hyperglycemiaCountLabel.setText(values[1]);
                hypoglycemiaCountLabel.setText(values[2]);
                averageDayLabel.setText(values[3]);
                averageWeekLabel.setText(values[4]);
                averageMonthLabel.setText(values[5]);
                hba1cLabel.setText(values[6]);
            }
        }).execute();
    }

    private void initializeChart() {
        if (getContext() == null) {
            return;
        }

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
        openFragment(new StatisticFragment(), true);
    }

    private void openEntry() {
        openFragment(EntryEditFragmentFactory.newInstance(latestEntry), true);
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
                openFragment(new EntryEditFragment(), true);
            }
        }, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        if (isAdded()) {
            updateContent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        if (isAdded()) {
            updateContent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        if (isAdded()) {
            updateContent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        if (isAdded()) {
            updateContent();
        }
    }
}
