package com.faltenreich.diaguard.ui.view.chart;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ChartHelper;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;
import java.util.List;

import static com.faltenreich.diaguard.DiaguardApplication.getContext;

/**
 * Created by Faltenreich on 15.03.2017
 */

class DayChartData extends CombinedData {

    private static final String TAG = DayChartData.class.getSimpleName();

    private static final String DATA_SET_BLOODSUGAR = "bloodSugar";
    private static final String DATA_SET_BLOODSUGAR_HYPERGLYCEMIA = "hyperglycemia";
    private static final String DATA_SET_BLOODSUGAR_HYPOGLYCEMIA = "hypoglycemia";
    private static final String DATA_SET_LINE = "line";

    private DayChart chart;

    DayChartData(DayChart chart, List<String> xLabels) {
        super(xLabels);
        this.chart = chart;
        setup();
    }

    private void setup() {
        setData(createLineData());
        setData(createScatterData());
    }

    void addEntry(Entry entry, PreferenceHelper.ChartStyle chartStyle) {
        float yValue = entry.getVal();
        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
            if (yValue > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                addEntry(DATA_SET_BLOODSUGAR_HYPERGLYCEMIA, entry, chartStyle);
            } else if (yValue < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                addEntry(DATA_SET_BLOODSUGAR_HYPOGLYCEMIA, entry, chartStyle);
            } else {
                addEntry(DATA_SET_BLOODSUGAR, entry, chartStyle);
            }
        } else {
            addEntry(DATA_SET_BLOODSUGAR, entry, chartStyle);
        }
    }

    private void addEntry(String chartLabel, Entry entry, PreferenceHelper.ChartStyle chartStyle) {
        switch (chartStyle) {
            case LINE: chart.getLineData().addEntry(entry, 0);
            case POINT: chart.getScatterData().getDataSetByLabel(chartLabel, true).addEntry(entry); break;
            default: Log.e(TAG, "Cannot add entry to unsupported chart style");
        }
    }

    private ScatterData createScatterData() {
        ScatterData scatterData = new ScatterData();
        scatterData.addDataSet(createScatterDataSet(DATA_SET_BLOODSUGAR, R.color.green));
        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
            scatterData.addDataSet(createScatterDataSet(DATA_SET_BLOODSUGAR_HYPERGLYCEMIA, R.color.red));
            scatterData.addDataSet(createScatterDataSet(DATA_SET_BLOODSUGAR_HYPOGLYCEMIA, R.color.blue));
        }
        return scatterData;
    }

    private LineData createLineData() {
        LineData lineData = new LineData();
        lineData.addDataSet(createLineDataSet());
        return lineData;
    }

    private ScatterDataSet createScatterDataSet(String title, @ColorRes int colorResourceId) {
        ScatterDataSet dataSet = new ScatterDataSet(new ArrayList<Entry>(), title);
        int dataSetColor = ContextCompat.getColor(getContext(), colorResourceId);
        dataSet.setColor(dataSetColor);
        dataSet.setScatterShapeSize(ChartHelper.SCATTER_SIZE);
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        dataSet.setDrawValues(false);
        return dataSet;
    }

    private LineDataSet createLineDataSet() {
        LineDataSet dataSet = new LineDataSet(new ArrayList<Entry>(), DATA_SET_LINE);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.green));
        dataSet.setLineWidth(ChartHelper.LINE_WIDTH);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawCubic(true);
        return dataSet;
    }
}
