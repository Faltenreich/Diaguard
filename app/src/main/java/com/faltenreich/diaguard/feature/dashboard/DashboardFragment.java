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
import com.faltenreich.diaguard.feature.dashboard.value.DashboardValue;
import com.faltenreich.diaguard.feature.dashboard.value.DashboardValueTask;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.datetime.TimeSpan;
import com.faltenreich.diaguard.feature.entry.edit.EntryEditFragment;
import com.faltenreich.diaguard.feature.navigation.FabDescribing;
import com.faltenreich.diaguard.feature.navigation.FabDescription;
import com.faltenreich.diaguard.feature.navigation.FabProperties;
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
import com.faltenreich.diaguard.shared.view.resource.ColorUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Minutes;

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding> implements ToolbarDescribing, FabDescribing {

    private ViewGroup latestLayout;
    private TextView latestValueLabel;
    private ImageView latestTrendImageView;
    private TextView latestAgoLabel;

    private ViewGroup todayLayout;
    private TextView totalCountLabel;
    private TextView targetCountLabel;
    private TextView hyperglycemiaCountLabel;
    private TextView hypoglycemiaCountLabel;

    private ViewGroup averageLayout;
    private TextView averageDayLabel;
    private TextView averageWeekLabel;
    private TextView averageMonthLabel;
    private TextView averageQuarterLabel;

    private ViewGroup trendLayout;
    private LineChart chartView;

    private ViewGroup hba1cLayout;
    private TextView hba1cLabel;
    private TextView hba1cValue;

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
        latestTrendImageView = getBinding().latestTrendImageView;
        latestAgoLabel = getBinding().latestAgoLabel;

        todayLayout = getBinding().todayLayout;
        totalCountLabel = getBinding().totalCountLabel;
        targetCountLabel = getBinding().targetCountLabel;
        hyperglycemiaCountLabel = getBinding().hyperglycemiaCountLabel;
        hypoglycemiaCountLabel = getBinding().hypoglycemiaCountLabel;

        averageLayout = getBinding().averageLayout;
        averageDayLabel = getBinding().averageDayLabel;
        averageWeekLabel = getBinding().averageWeekLabel;
        averageMonthLabel = getBinding().averageMonthLabel;
        averageQuarterLabel = getBinding().averageQuarterLabel;

        trendLayout = getBinding().trendLayout;
        chartView = getBinding().chartView;

        hba1cLayout = getBinding().hba1cLayout;
        hba1cLabel = getBinding().hba1cLabel;
        hba1cValue = getBinding().hba1cValue;

        alarmLayout = getBinding().alarmLayout.alarmLayout;
        alarmLabel = getBinding().alarmLayout.alarmLabel;
        alarmDeleteButton = getBinding().alarmLayout.alarmDeleteButton;
    }

    private void initLayout() {
        latestLayout.setOnClickListener((view) -> openEntry(latestEntry));
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
            BloodSugar bloodSugar = (BloodSugar) MeasurementDao.getInstance(BloodSugar.class).getMeasurement(latestEntry);

            // Value
            latestValueLabel.setText(bloodSugar.toString());
            if (bloodSugar.getTrend() != null) {
                latestTrendImageView.setImageResource(bloodSugar.getTrend().iconRes);
            } else {
                latestTrendImageView.setImageDrawable(null);
            }

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
            int differenceInMinutes = Minutes.minutesBetween(latestEntry.getDate(), new DateTime()).getMinutes();
            latestAgoLabel.setText(Helper.getTextAgo(getActivity(), differenceInMinutes));
            int latestAgoLabelTextColor = differenceInMinutes > DateTimeConstants.MINUTES_PER_HOUR * 8
                ? ContextCompat.getColor(getContext(), R.color.red)
                : ColorUtils.getTextColorSecondary(getContext());
            latestAgoLabel.setTextColor(latestAgoLabelTextColor);
        } else {
            latestValueLabel.setText(R.string.placeholder);
            latestValueLabel.setTextColor(ColorUtils.getTextColorPrimary(getContext()));
            latestTrendImageView.setImageDrawable(null);

            latestAgoLabel.setText(R.string.first_visit_desc);
            latestAgoLabel.setTextColor(ColorUtils.getTextColorSecondary(getContext()));
        }
    }

    private void updateDashboard() {
        new DashboardValueTask(getContext(), values -> {
            if (isAdded() && values != null && values.length == 9) {
                totalCountLabel.setText(values[0].getValue());
                targetCountLabel.setText(values[1].getValue());
                hyperglycemiaCountLabel.setText(values[2].getValue());
                hypoglycemiaCountLabel.setText(values[3].getValue());
                averageDayLabel.setText(values[4].getValue());
                averageWeekLabel.setText(values[5].getValue());
                averageMonthLabel.setText(values[6].getValue());
                averageQuarterLabel.setText(values[7].getValue());

                DashboardValue hba1c = values[8];
                hba1cLabel.setText(hba1c.getKey());
                hba1cValue.setText(hba1c.getValue());
                if (hba1c.getEntry() != null) {
                    hba1cLayout.setOnClickListener((view) -> openEntry(hba1c.getEntry()));
                } else {
                    hba1cLayout.setOnClickListener((view) -> showHbA1cFormula());
                }
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
        chartView.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int daysPast = -(timeSpan.stepsPerInterval - (int) value);
                DateTime dateTime = timeSpan.getStep(DateTime.now(), daysPast);
                return timeSpan.getLabel(dateTime);
            }
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

    private void openEntry(Entry entry) {
        openFragment(EntryEditFragment.newInstance(entry), true);
    }

    private void showHbA1cFormula() {
        String formula = String.format(getString(R.string.hba1c_formula),
                getString(R.string.average_symbol),
                getString(R.string.months),
                getString(R.string.bloodsugar));
        ViewUtils.showSnackbar(getView(), formula);
    }

    @Override
    public FabDescription getFabDescription() {
        return new FabDescription(FabProperties.addButton(view -> openFragment(new EntryEditFragment(), true)), false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryAddedEvent event) {
        updateContent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryUpdatedEvent event) {
        updateContent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EntryDeletedEvent event) {
        updateContent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UnitChangedEvent event) {
        updateContent();
    }
}
