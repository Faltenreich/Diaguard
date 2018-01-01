package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.support.annotation.ColorInt;

import com.faltenreich.diaguard.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;

/**
 * Created by Faltenreich on 04.11.2017
 */

class DayChartScatterDataSet extends ScatterDataSet {

    DayChartScatterDataSet(Context context, String label, @ColorInt int color) {
        super(new ArrayList<Entry>(), label);
        setColor(color);
        setScatterShapeSize(context.getResources().getDimension(R.dimen.chart_scatter_size));
        setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
