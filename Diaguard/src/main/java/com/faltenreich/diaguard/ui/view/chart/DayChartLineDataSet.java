package com.faltenreich.diaguard.ui.view.chart;

import androidx.annotation.ColorInt;

import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class DayChartLineDataSet extends LineDataSet {

    DayChartLineDataSet(String label, @ColorInt int color) {
        super(new ArrayList<>(), label);
        setColor(color);
        setLineWidth(ChartHelper.LINE_WIDTH);
        setDrawCircles(false);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
