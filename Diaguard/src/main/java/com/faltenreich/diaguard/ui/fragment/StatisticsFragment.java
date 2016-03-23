package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.event.Events;
import com.faltenreich.diaguard.util.event.ui.TimeSpanChangedEvent;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.UpdateChartTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;

import butterknife.Bind;

/**
 * Created by Faltenreich on 16.03.2016.
 */
public class StatisticsFragment extends BaseFragment {

    public static final String BUNDLE_ARGUMENT_CATEGORY = "BUNDLE_ARGUMENT_CATEGORY";
    public static final String BUNDLE_ARGUMENT_TIME_SPAN = "BUNDLE_ARGUMENT_TIME_SPAN";

    public static StatisticsFragment newInstance(Measurement.Category category, TimeSpan timeSpan) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(StatisticsFragment.BUNDLE_ARGUMENT_CATEGORY, category);
        bundle.putSerializable(StatisticsFragment.BUNDLE_ARGUMENT_TIME_SPAN, timeSpan);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.statistics_chart_trend)
    protected LineChart chart;

    private Measurement.Category category;
    private TimeSpan timeSpan;

    public StatisticsFragment() {
        super(R.layout.fragment_statistics, R.string.statistics);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
        updateContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Events.unregister(this);
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.category = (Measurement.Category) bundle.getSerializable(BUNDLE_ARGUMENT_CATEGORY);
            this.timeSpan = (TimeSpan) bundle.getSerializable(BUNDLE_ARGUMENT_TIME_SPAN);
        }
    }

    private void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    private void updateContent() {
        updateChart();
    }

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
        new UpdateChartTask(getContext(), new BaseAsyncTask.OnAsyncProgressListener<LineData>() {
            @Override
            public void onPostExecute(LineData lineData) {
                if(isAdded()) {
                    chart.setData(lineData);
                    chart.invalidate();
                }
            }
        }, category, timeSpan).execute();
    }

    // endregion

    @SuppressWarnings("unused")
    public void onEvent(TimeSpanChangedEvent event) {
        setTimeSpan(event.context);
        updateContent();
    }
}
