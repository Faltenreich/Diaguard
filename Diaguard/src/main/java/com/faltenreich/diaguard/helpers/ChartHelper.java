package com.faltenreich.diaguard.helpers;

import android.graphics.Color;
import android.graphics.Paint;

import com.faltenreich.diaguard.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;

/**
 * Created by Filip on 30.06.2015.
 */
public class ChartHelper {

    public static final int TEXT_SIZE = 4;
    public static final float CIRCLE_SIZE = 1.6f;
    public static final float LINE_WIDTH = 0.8f;

    public static void setChartDefaultStyle(BarLineChartBase chart) {
        // General
        chart.setDrawGridBackground(false);
        chart.setHighlightEnabled(false);
        chart.setPinchZoom(true);

        // Text
        chart.getLegend().setEnabled(false);
        chart.setDescription(null);
        chart.setNoDataText(chart.getContext().getString(R.string.no_data));
        chart.getXAxis().setTextSize(Helper.getDPI(TEXT_SIZE));
        chart.getAxisLeft().setTextSize(Helper.getDPI(TEXT_SIZE));
        Paint infoTextColor = chart.getPaint(Chart.PAINT_INFO);
        infoTextColor.setColor(chart.getResources().getColor(R.color.gray_dark));
        chart.setPaint(infoTextColor, Chart.PAINT_INFO);

        // Axes
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setGridColor(chart.getContext().getResources().getColor(android.R.color.darker_gray));
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setGridColor(chart.getContext().getResources().getColor(android.R.color.darker_gray));
        chart.getAxisRight().setEnabled(false);
    }
}
