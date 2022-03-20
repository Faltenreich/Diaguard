package com.faltenreich.diaguard.feature.timeline.chart;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.shared.view.chart.ChartUtils;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

class DayChartLineDataSet extends LineDataSet {

    DayChartLineDataSet(Context context, String label, @ColorRes int colorResource) {
        super(new ArrayList<>(), label);
        setColor(ContextCompat.getColor(context, colorResource));
        setLineWidth(ChartUtils.LINE_WIDTH);
        setDrawCircles(false);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
