package com.faltenreich.diaguard.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.thread.BaseAsyncTask;
import com.faltenreich.diaguard.util.thread.UpdateChartTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;

import butterknife.Bind;

/**
 * Created by Faltenreich on 16.03.2016.
 */
public class TrendFragment extends BaseFragment {

    @Bind(R.id.trend_chart)
    protected LineChart chart;

    public TrendFragment() {
        super(R.layout.fragment_trend, R.string.trend);
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
        updateChart();
    }

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
        }, UpdateChartTask.TimeSpan.WEEK).execute();
    }
}
