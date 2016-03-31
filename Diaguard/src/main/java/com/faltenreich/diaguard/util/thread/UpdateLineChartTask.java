package com.faltenreich.diaguard.util.thread;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.TimeSpan;
import com.faltenreich.diaguard.util.ViewHelper;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class UpdateLineChartTask extends BaseAsyncTask<Void, Void, LineData> {

    private Measurement.Category category;
    private TimeSpan timeSpan;
    private int dataSetColor;

    public UpdateLineChartTask(Context context, OnAsyncProgressListener<LineData> onAsyncProgressListener, Measurement.Category category, TimeSpan timeSpan) {
        super(context, onAsyncProgressListener);
        this.category = category;
        this.timeSpan = timeSpan;
        this.dataSetColor = ContextCompat.getColor(context, R.color.green_light);
    }

    protected LineData doInBackground(Void... params) {
        DateTime endDateTime = DateTime.now().withTime(23, 59, 59, 999);
        DateTime startDateTime = timeSpan.getPastInterval(endDateTime).getStart();
        startDateTime = startDateTime.plusDays(1).withTimeAtStartOfDay();

        List<Entry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();

        float targetValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getTargetValue());
        float highestValue = targetValue * 2;

        DateTime intervalStart = startDateTime;
        int index = 0;
        while (!intervalStart.isAfter(endDateTime)) {
            DateTime intervalEnd = timeSpan.getNextInterval(intervalStart, 1).minusDays(1);
            boolean showLabel = ViewHelper.isLargeScreen(getContext()) || timeSpan != TimeSpan.YEAR || index % 2 == 0;
            xLabels.add(showLabel ? timeSpan.getLabel(intervalStart) : "");
            float avg = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, new Interval(intervalStart, intervalEnd));
            if (avg > 0) {
                entries.add(new com.github.mikephil.charting.data.Entry(avg, index));
                if (avg > highestValue) {
                    highestValue = avg;
                }
            }
            intervalStart = timeSpan.getNextInterval(intervalStart, 1);
            index++;
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(entries, BloodSugar.class.getSimpleName());
        dataSet.setColor(dataSetColor);
        dataSet.setCircleColor(dataSetColor);
        dataSet.setCircleSize(ChartHelper.CIRCLE_SIZE);
        dataSet.setDrawCircles(entries.size() <= 1);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(ChartHelper.LINE_WIDTH);
        dataSets.add(dataSet);

        // Workaround to set visible area
        List<com.github.mikephil.charting.data.Entry> entriesMaximum = new ArrayList<>();
        entriesMaximum.add(new com.github.mikephil.charting.data.Entry(highestValue, xLabels.size()));
        LineDataSet dataSetMaximum = new LineDataSet(entriesMaximum, "Maximum");
        dataSetMaximum.setColor(Color.TRANSPARENT);
        dataSets.add(dataSetMaximum);

        return new LineData(xLabels, dataSets);
    }
}