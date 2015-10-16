package com.faltenreich.diaguard.util;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by Filip on 30.06.2015.
 */
public class ChartHelper {

    public static final float VIEW_PORT_OFFSET = Helper.getDPI(2);
    public static final float TEXT_SIZE = Helper.getDPI(5);
    public static final float SCATTER_SIZE = Helper.getDPI(5);
    public static final float CIRCLE_SIZE = Helper.getDPI(1.6f);
    public static final float LINE_WIDTH = 1f;

    public static void setChartDefaultStyle(BarLineChartBase chart) {

        // General
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setHighlightEnabled(true);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setExtraLeftOffset(VIEW_PORT_OFFSET);
        chart.setExtraBottomOffset(VIEW_PORT_OFFSET);

        // Text
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);
        chart.setNoDataText("");
        chart.getXAxis().setTextSize(TEXT_SIZE);
        chart.getAxisLeft().setTextSize(TEXT_SIZE);

        // Axes
        int gridColor = ContextCompat.getColor(chart.getContext(), android.R.color.darker_gray);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setGridColor(gridColor);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setGridColor(gridColor);
        chart.getAxisLeft().setDrawLimitLinesBehindData(true);

        float yAxisMinValue = PreferenceHelper.getInstance().getExtrema(Measurement.Category.BLOODSUGAR)[0] - 5;
        chart.getAxisLeft().setAxisMinValue(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yAxisMinValue));
        chart.getAxisLeft().setStartAtZero(false);
        chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis axis) {
                return PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(value);
            }
        });
    }
}
