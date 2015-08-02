package com.faltenreich.diaguard.ui.chart;

import android.graphics.Color;
import android.graphics.Paint;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.faltenreich.diaguard.helpers.PreferenceHelper;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by Filip on 30.06.2015.
 */
public class ChartHelper {

    public static final float VIEW_PORT_OFFSET = Helper.getDPI(2);
    public static final float TEXT_SIZE = Helper.getDPI(5);
    public static final float SCATTER_SIZE = Helper.getDPI(4);
    public static final float CIRCLE_SIZE = Helper.getDPI(1.6f);
    public static final float LINE_WIDTH = 1f;

    public static void setChartDefaultStyle(BarLineChartBase chart) {
        // General
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.setHighlightEnabled(false);
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
        int gridColor = chart.getContext().getResources().getColor(android.R.color.darker_gray);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setGridColor(gridColor);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setGridColor(gridColor);
        chart.getAxisLeft().setDrawLimitLinesBehindData(true);
        chart.getAxisLeft().setAxisMaxValue(PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BloodSugar, 275));
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return PreferenceHelper.getInstance().getDecimalFormat(Measurement.Category.BloodSugar).format(value);
            }
        });
    }
}
