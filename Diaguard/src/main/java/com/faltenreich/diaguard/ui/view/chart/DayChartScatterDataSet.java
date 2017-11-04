package com.faltenreich.diaguard.ui.view.chart;

import android.support.annotation.ColorInt;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class DayChartScatterDataSet extends ScatterDataSet {

    private static final float SHAPE_SIZE = 40;

    DayChartScatterDataSet(String label, @ColorInt int color) {
        super(new ArrayList<Entry>(), label);
        setColor(color);
        setScatterShapeSize(SHAPE_SIZE);
        setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        setDrawValues(false);
    }
}
