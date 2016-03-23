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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 16.03.2016.
 */

public class UpdateChartTask extends BaseAsyncTask<Void, Void, LineData> {

    private Measurement.Category category;
    private TimeSpan timeSpan;

    private int dataSetColor;
    private String[] weekDays;

    public UpdateChartTask(Context context, OnAsyncProgressListener<LineData> onAsyncProgressListener, Measurement.Category category, TimeSpan timeSpan) {
        super(context, onAsyncProgressListener);
        this.category = category;
        this.timeSpan = timeSpan;
        this.dataSetColor = ContextCompat.getColor(context, R.color.green_light);
        this.weekDays = context.getResources().getStringArray(R.array.weekdays_short);
    }

    protected LineData doInBackground(Void... params) {
        DateTime endDateTime = DateTime.now();
        DateTime startDateTime;
        switch (timeSpan) {
            case WEEK:
                startDateTime = endDateTime.minusWeeks(1);
                break;
            case TWO_WEEKS:
                startDateTime = endDateTime.minusWeeks(2);
                break;
            case MONTH:
                startDateTime = endDateTime.minusMonths(1);
                break;
            case YEAR:
                startDateTime = endDateTime.minusYears(1);
                break;
            default:
                startDateTime = endDateTime;
        }
        startDateTime = startDateTime.plusDays(1);

        List<Entry> entries = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();

        float targetValue = PreferenceHelper.getInstance().formatDefaultToCustomUnit(
                Measurement.Category.BLOODSUGAR,
                PreferenceHelper.getInstance().getTargetValue());
        float highestValue = targetValue * 2;

        DateTime currentDay = startDateTime;
        while (!currentDay.isAfter(endDateTime)) {
            int index = Days.daysBetween(startDateTime, currentDay).getDays();
            int weekDayIndex = currentDay.dayOfWeek().get() - 1;
            if (weekDayIndex >= 0 && weekDayIndex < weekDays.length) {
                xLabels.add(index, weekDays[weekDayIndex]);
                float avg = MeasurementDao.getInstance(BloodSugar.class).avg(BloodSugar.Column.MGDL, currentDay);
                if (avg > 0) {
                    entries.add(new com.github.mikephil.charting.data.Entry(avg, index));
                    if (avg > highestValue) {
                        highestValue = avg;
                    }
                }
            }
            currentDay = currentDay.plusDays(1);
        }

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
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