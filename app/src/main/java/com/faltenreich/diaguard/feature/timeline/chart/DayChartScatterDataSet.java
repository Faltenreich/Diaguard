package com.faltenreich.diaguard.feature.timeline.chart;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;

class DayChartScatterDataSet extends ScatterDataSet {

    DayChartScatterDataSet(Context context, String label, @ColorRes int colorResource) {
        super(new ArrayList<>(), label);
        setColor(ContextCompat.getColor(context, colorResource));
        setScatterShapeSize(context.getResources().getDimension(R.dimen.chart_scatter_size));
        setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        setDrawValues(false);
        setDrawHighlightIndicators(false);
    }
}
