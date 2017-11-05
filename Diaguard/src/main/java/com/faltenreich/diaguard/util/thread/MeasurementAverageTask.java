package com.faltenreich.diaguard.util.thread;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ChartHelper;
import com.faltenreich.diaguard.util.NumberUtils;
import com.faltenreich.diaguard.util.TimeSpan;
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

public class MeasurementAverageTask extends BaseAsyncTask<Void, Void, LineData> {

    private static final String TAG = MeasurementAverageTask.class.getSimpleName();
    private static final float CIRCLE_RADIUS = 4;

    private Measurement.Category category;
    private TimeSpan timeSpan;
    private int dataSetColor;
    private boolean forceDrawing;
    private boolean fillDrawing;

    public MeasurementAverageTask(Context context, OnAsyncProgressListener<LineData> onAsyncProgressListener, Measurement.Category category, TimeSpan timeSpan, boolean forceDrawing, boolean fillDrawing) {
        super(context, onAsyncProgressListener);
        this.category = category;
        this.timeSpan = timeSpan;
        this.forceDrawing = forceDrawing;
        this.fillDrawing = fillDrawing;
        this.dataSetColor = ContextCompat.getColor(context, R.color.green_light);
    }

    @Override
    protected LineData doInBackground(Void... params) {
        List<Entry> entries = new ArrayList<>();

        DateTime endDateTime = DateTime.now().withTime(23, 59, 59, 999);
        DateTime startDateTime = timeSpan.getInterval(endDateTime, -1).getStart();
        startDateTime = startDateTime.withTimeAtStartOfDay();

        int index = 0;
        float highestValue = 0;
        DateTime intervalStart = startDateTime;
        while (!intervalStart.isAfter(endDateTime)) {
            DateTime intervalEnd = timeSpan.getStep(intervalStart, 1).minusDays(1);
            Measurement measurement = MeasurementDao.getInstance(category.toClass()).getAvgMeasurement(category, new Interval(intervalStart, intervalEnd));
            if (measurement != null) {
                for (Float avg : measurement.getValues()) {
                    if (NumberUtils.isValid(avg) && avg > 0) {
                        float yValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(category, avg);
                        entries.add(new Entry(index, yValue));
                        if (yValue > highestValue) {
                            highestValue = yValue;
                        }
                    }
                }
            }
            intervalStart = timeSpan.getStep(intervalStart, 1);
            index++;
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if (entries.size() > 0) {
            LineDataSet dataSet = new LineDataSet(entries, "Blood sugar average per day");
            dataSet.setColor(dataSetColor);
            dataSet.setLineWidth(fillDrawing ? 0 : ChartHelper.LINE_WIDTH);
            dataSet.setCircleColor(dataSetColor);
            dataSet.setCircleRadius(CIRCLE_RADIUS);
            dataSet.setDrawCircles(entries.size() <= 1);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(fillDrawing);
            dataSet.setFillColor(dataSetColor);
            dataSets.add(dataSet);
        }

        // Workaround to set visible area
        if (forceDrawing) {
            if (category == Measurement.Category.BLOODSUGAR) {
                float targetValue = PreferenceHelper.getInstance().
                        formatDefaultToCustomUnit(Measurement.Category.BLOODSUGAR,
                                PreferenceHelper.getInstance().getTargetValue());
                targetValue = targetValue * 2;
                if (highestValue == 0 || highestValue < targetValue) {
                    highestValue = targetValue;
                }
            }
            List<com.github.mikephil.charting.data.Entry> entriesMaximum = new ArrayList<>();
            entriesMaximum.add(new com.github.mikephil.charting.data.Entry(-1, highestValue));
            LineDataSet dataSetMaximum = new LineDataSet(entriesMaximum, "Fake");
            dataSetMaximum.setColor(Color.TRANSPARENT);
            dataSets.add(dataSetMaximum);
        }

        try {
            return new LineData(dataSets);
        } catch (IllegalArgumentException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }
}