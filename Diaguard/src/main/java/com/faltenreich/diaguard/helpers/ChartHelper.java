package com.faltenreich.diaguard.helpers;

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
    public static final int SCATTER_SHAPE_SIZE = 5;

    public static void setChartDefaultStyle(BarLineChartBase chartBase) {
        // General
        chartBase.setDrawGridBackground(false);
        chartBase.setHighlightEnabled(false);
        chartBase.setDoubleTapToZoomEnabled(false);
        chartBase.setScaleEnabled(false);

        // Text
        chartBase.getLegend().setEnabled(false);
        chartBase.setDescription(null);
        chartBase.setNoDataText(chartBase.getContext().getString(R.string.no_data));
        chartBase.getXAxis().setTextSize(Helper.getDPI(TEXT_SIZE));
        chartBase.getAxisLeft().setTextSize(Helper.getDPI(TEXT_SIZE));
        Paint infoTextColor = chartBase.getPaint(Chart.PAINT_INFO);
        infoTextColor.setColor(chartBase.getResources().getColor(R.color.gray_dark));
        chartBase.setPaint(infoTextColor, Chart.PAINT_INFO);

        // Axes
        chartBase.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chartBase.getXAxis().setDrawAxisLine(false);
        chartBase.getXAxis().setGridColor(chartBase.getContext().getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisLeft().setDrawAxisLine(false);
        chartBase.getAxisLeft().setGridColor(chartBase.getContext().getResources().getColor(android.R.color.darker_gray));
        chartBase.getAxisRight().setEnabled(false);
    }
}
