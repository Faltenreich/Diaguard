package com.faltenreich.diaguard.ui.view.chart;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ArrayUtils;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

/**
 * Created by Faltenreich on 15.03.2017
 */

class DayChartData extends CombinedData {

    private enum DataSetType {
        TARGET("target", R.color.green),
        HYPER("hyperglycemia", R.color.red),
        HYPO("hypoglycemia", R.color.blue);

        public String label;
        public @ColorRes int colorResId;

        DataSetType(String label, @ColorRes int colorResId) {
            this.label = label;
            this.colorResId = colorResId;
        }
    }

    private Context context;
    private PreferenceHelper.ChartStyle chartStyle;
    private List<Measurement> values;

    DayChartData(Context context, List<Measurement> values) {
        super();
        this.context = context;
        this.chartStyle = PreferenceHelper.getInstance().getChartStyle();
        this.values = values;
        init();
    }

    private void init() {
        if (values.size() > 0) {
            for (Measurement value : values) {
                int xValue = value.getEntry().getDate().getMinuteOfDay();
                float yValue = ArrayUtils.sum(value.getValues());
                yValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR, yValue);
                Entry chartEntry = new Entry(xValue, yValue, value.getEntry());
                addEntry(chartEntry);
            }
        } else {
            // Add fake entry to display empty chart
            addEntry(new Entry(-1, 0));
        }
    }

    @Override
    public LineData getLineData() {
        LineData lineData = super.getLineData();
        if (lineData == null) {
            lineData = new LineData();
            setData(lineData);
        }
        return lineData;
    }

    @Override
    public ScatterData getScatterData() {
        ScatterData scatterData = super.getScatterData();
        if (scatterData == null) {
            scatterData = new ScatterData();
            setData(scatterData);
        }
        return scatterData;
    }

    private ILineDataSet getLineDataSet() {
        if (getLineData().getDataSetCount() == 0) {
            DataSetType type = DataSetType.TARGET;
            ILineDataSet dataSet = new DayChartLineDataSet(type.label, ContextCompat.getColor(context, type.colorResId));
            getLineData().addDataSet(dataSet);
            return dataSet;
        } else {
            return getLineData().getDataSetByIndex(0);
        }
    }

    private IScatterDataSet getScatterDataSet(DataSetType type) {
        IScatterDataSet dataSet = getScatterData().getDataSetByLabel(type.label, true);
        if (dataSet == null) {
            dataSet = new DayChartScatterDataSet(type.label, ContextCompat.getColor(context, type.colorResId));
            getScatterData().addDataSet(dataSet);
        }
        return dataSet;
    }

    private void addEntry(Entry entry, DataSetType type) {
        switch (chartStyle) {
            case LINE:
                getLineDataSet().addEntry(entry);
                // No break, because points are added as well for line charts
            case POINT:
                getScatterDataSet(type).addEntry(entry);
                break;
            default:
                throw new IllegalArgumentException("Failed to add entry to chart style " + chartStyle);
        }
    }

    private void addEntry(Entry entry) {
        float yValue = entry.getY();
        if (PreferenceHelper.getInstance().limitsAreHighlighted()) {
            if (yValue > PreferenceHelper.getInstance().getLimitHyperglycemia()) {
                addEntry(entry, DataSetType.HYPER);
            } else if (yValue < PreferenceHelper.getInstance().getLimitHypoglycemia()) {
                addEntry(entry, DataSetType.HYPO);
            } else {
                addEntry(entry, DataSetType.TARGET);
            }
        } else {
            addEntry(entry, DataSetType.TARGET);
        }
    }
}