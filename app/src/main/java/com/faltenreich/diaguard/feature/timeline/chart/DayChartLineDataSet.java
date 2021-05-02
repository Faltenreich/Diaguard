package com.faltenreich.diaguard.feature.timeline.chart;

import androidx.annotation.ColorInt;

import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class DayChartLineDataSet extends LineDataSet {

    DayChartLineDataSet(String label, @ColorInt int color) {
        super(new ArrayList<>(), label);
        setColor(color);
        setLineWidth(ChartUtils.LINE_WIDTH);
        setDrawCircles(false);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
