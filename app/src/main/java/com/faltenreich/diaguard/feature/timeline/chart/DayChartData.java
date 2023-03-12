package com.faltenreich.diaguard.feature.timeline.chart;

import android.content.Context;

import androidx.annotation.ColorRes;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.primitive.ArrayUtils;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

public class DayChartData extends CombinedData {

    private static final float Y_MAX_VALUE_DEFAULT = 200;
    private static final float Y_MAX_VALUE_OFFSET = 20;
    private static final String LABEL_LINE = "line";

    private enum DataSetType {
        TARGET("target", R.color.green),
        HYPER("hyperglycemia", R.color.red),
        HYPO("hypoglycemia", R.color.blue);

        public final String label;
        public @ColorRes final int colorResId;

        DataSetType(String label, @ColorRes int colorResId) {
            this.label = label;
            this.colorResId = colorResId;
        }
    }

    private final Context context;
    private final boolean showDots;
    private final boolean showLines;
    private final List<Measurement> values;
    private float yAxisMaximum;

    public DayChartData(
        Context context,
        boolean showDots,
        boolean showLines,
        List<Measurement> values
    ) {
        super();
        this.context = context;
        this.showDots = showDots;
        this.showLines = showLines;
        this.values = values;
        createEntries();
        calculateYAxisMaximum();
    }

    public float getYAxisMaximum() {
        return yAxisMaximum;
    }

    private void createEntries() {
        if (values.size() > 0) {
            for (Measurement value : values) {
                int xValue = value.getEntry().getDate().getMinuteOfDay();
                float yValue = ArrayUtils.sum(value.getValues());
                yValue = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, yValue);
                addEntry(new Entry(xValue, yValue, value.getEntry()));
            }
        } else {
            // Add fake entry to display empty chart
            addEntry(new Entry(-1, 0));
        }
    }

    // Identify max value manually because data.getYMax does not work when combining scatter with line chart
    private void calculateYAxisMaximum() {
        yAxisMaximum = PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, Y_MAX_VALUE_DEFAULT);
        ChartData<?> data = showDots ? getScatterData() : getLineData();
        for (int datasetIndex = 0; datasetIndex < data.getDataSetCount(); datasetIndex++) {
            IDataSet<?> dataSet = data.getDataSetByIndex(datasetIndex);
            for (int entryIndex = 0; entryIndex < dataSet.getEntryCount(); entryIndex++) {
                float entryValue = dataSet.getEntryForIndex(entryIndex).getY();
                if (entryValue > yAxisMaximum) {
                    yAxisMaximum = entryValue;
                }
            }
        }
        yAxisMaximum += PreferenceStore.getInstance().formatDefaultToCustomUnit(Category.BLOODSUGAR, Y_MAX_VALUE_OFFSET);
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
            ILineDataSet dataSet = new DayChartLineDataSet(
                context,
                LABEL_LINE,
                android.R.color.white // TODO: Support dark mode
            );
            getLineData().addDataSet(dataSet);
            return dataSet;
        } else {
            return getLineData().getDataSetByIndex(0);
        }
    }

    private IScatterDataSet getScatterDataSet(DataSetType type) {
        IScatterDataSet dataSet = getScatterData().getDataSetByLabel(type.label, true);
        if (dataSet == null) {
            dataSet = new DayChartScatterDataSet(context, type.label, type.colorResId);
            getScatterData().addDataSet(dataSet);
        }
        return dataSet;
    }

    private void addEntry(Entry entry) {
        float yValue = entry.getY();
        if (PreferenceStore.getInstance().limitsAreHighlighted()) {
            if (yValue > PreferenceStore.getInstance().getLimitHyperglycemia()) {
                addEntry(entry, DataSetType.HYPER);
            } else if (yValue < PreferenceStore.getInstance().getLimitHypoglycemia()) {
                addEntry(entry, DataSetType.HYPO);
            } else {
                addEntry(entry, DataSetType.TARGET);
            }
        } else {
            addEntry(entry, DataSetType.TARGET);
        }
    }

    private void addEntry(Entry entry, DataSetType type) {
        if (showLines) {
            getLineDataSet().addEntry(entry);
        }
        // Show dot anyway if there is only one entry since it will not be connected with a line
        if (values.size() == 1 || showDots) {
            getScatterDataSet(type).addEntry(entry);
        }
    }
}