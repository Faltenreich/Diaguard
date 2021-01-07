package com.faltenreich.diaguard.feature.timeline.chart;

import android.content.Context;

import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.async.BaseAsyncTask;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faltenreich on 05.11.2017
 */
class DayChartDataFetchTask extends BaseAsyncTask<Void, Void, DayChartData> {

    private DateTime day;

    DayChartDataFetchTask(Context context, OnAsyncProgressListener<DayChartData> onAsyncProgressListener, DateTime day) {
        super(context, onAsyncProgressListener);
        this.day = day;
    }

    @Override
    protected DayChartData doInBackground(Void... params) {
        List<Measurement> values = new ArrayList<>();
        List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(day);
        if (entries != null && entries.size() > 0) {
            for (com.faltenreich.diaguard.shared.data.database.entity.Entry entry : entries) {
                // TODO: Improve performance by using transaction / bulk fetch
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry, new Category[] { Category.BLOODSUGAR });
                values.addAll(measurements);
            }
        }
        return new DayChartData(getContext(), values);
    }
}