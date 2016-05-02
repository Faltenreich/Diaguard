package com.faltenreich.diaguard.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by Filip on 30.06.2015.
 */
public class ChartHelper {

    public static final int NO_DATA_TEXT_RESOURCE_ID = R.string.no_data;
    public static final int NO_DATA_COLOR_RESOURCE_ID = R.color.gray_darker;

    public static final float VIEW_PORT_OFFSET = 10;
    public static final float TEXT_SIZE = 14;
    public static final float SCATTER_SIZE = 14;
    public static final float CIRCLE_SIZE = 6;
    public static final float LINE_WIDTH = 3;

    public static void setChartDefaultStyle(BarLineChartBase chart, final Measurement.Category category) {
        Context context = chart.getContext();
        int textColor = ContextCompat.getColor(context, R.color.gray_darker);
        int gridColor = ContextCompat.getColor(context, android.R.color.darker_gray);

        // General
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setExtraLeftOffset(VIEW_PORT_OFFSET);
        chart.setExtraBottomOffset(VIEW_PORT_OFFSET);

        // Text
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);
        chart.getXAxis().setTextSize(TEXT_SIZE);
        chart.getAxisLeft().setTextSize(TEXT_SIZE);
        chart.setNoDataText(context.getString(NO_DATA_TEXT_RESOURCE_ID));
        chart.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(context, NO_DATA_COLOR_RESOURCE_ID));

        // Axes
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setGridColor(gridColor);
        chart.getXAxis().setTextColor(textColor);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setGridColor(gridColor);
        chart.getAxisLeft().setTextColor(textColor);
        chart.getAxisLeft().setDrawLimitLinesBehindData(true);
        float yAxisMinValue = PreferenceHelper.getInstance().getExtrema(category)[0] * .9f;
        float yAxisMinCustomValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, yAxisMinValue);
        chart.getAxisLeft().setAxisMinValue(yAxisMinCustomValue);
        chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis axis) {
                return PreferenceHelper.getInstance().getDecimalFormat(category).format(value);
            }
        });
    }

    public static LimitLine getLimitLine(Context context, float yValue, @ColorRes int color) {
        LimitLine limitLine = new LimitLine(yValue, null);
        limitLine.setLineColor(ContextCompat.getColor(context, color));
        limitLine.setLabel(null);
        return limitLine;
    }
}
