package com.faltenreich.diaguard.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by Filip on 30.06.2015.
 */
public class ChartHelper {

    public static final float VIEW_PORT_OFFSET = 10;
    public static final float TEXT_SIZE = 14;
    public static final float SCATTER_SIZE = 14;
    public static final float CIRCLE_SIZE = 6;
    public static final float LINE_WIDTH = 3;

    public static void setChartDefaultStyle(BarLineChartBase chart) {
        Context context = chart.getContext();

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
        chart.setNoDataText("");
        chart.getXAxis().setTextSize(TEXT_SIZE);
        chart.getAxisLeft().setTextSize(TEXT_SIZE);
        chart.setNoDataText(context.getString(R.string.no_data));
        chart.getPaint(Chart.PAINT_INFO).setColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        // Axes
        int gridColor = ContextCompat.getColor(chart.getContext(), android.R.color.darker_gray);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setGridColor(gridColor);
        chart.getXAxis().setTextColor(ContextCompat.getColor(context, R.color.gray_dark));
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setGridColor(gridColor);
        chart.getAxisLeft().setDrawLimitLinesBehindData(true);
        chart.getAxisLeft().setAxisMinValue(0f);
        chart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis axis) {
                return PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BLOODSUGAR).format(value);
            }
        });
    }
}
