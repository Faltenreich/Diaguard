package com.faltenreich.diaguard.util.thread;

import android.content.Context;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.ui.view.chart.DayChartData;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 05.11.2017
 */

public class BloodSugarDayTask extends BaseAsyncTask<Void, Void, DayChartData> {

    private DateTime day;

    public BloodSugarDayTask(Context context, OnAsyncProgressListener<DayChartData> onAsyncProgressListener, DateTime day) {
        super(context, onAsyncProgressListener);
        this.day = day;
    }

    protected DayChartData doInBackground(Void... params) {
        List<Measurement> values = new ArrayList<>();
        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
        if (entries != null && entries.size() > 0) {
            for (com.faltenreich.diaguard.data.entity.Entry entry : entries) {
                // TODO: Improve performance by using transaction / bulk fetch
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, new Measurement.Category[] { Measurement.Category.BLOODSUGAR });
                values.addAll(measurements);
            }
        }
        return new DayChartData(getContext(), values);
    }
}