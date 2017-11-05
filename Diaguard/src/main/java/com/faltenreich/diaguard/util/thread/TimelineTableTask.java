package com.faltenreich.diaguard.util.thread;

import android.content.Context;

import com.faltenreich.diaguard.adapter.list.ListItemCategoryValues;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Measurement;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Faltenreich on 05.11.2017
 */

public class TimelineTableTask extends BaseAsyncTask<Void, Void, List<ListItemCategoryValues>> {

    private static final int SKIP_EVERY_X_HOUR = 2;

    private DateTime day;
    private Measurement.Category[] categories;

    public TimelineTableTask(Context context, DateTime day, Measurement.Category[] categories, OnAsyncProgressListener<List<ListItemCategoryValues>> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
        this.day = day;
        this.categories = categories;
    }

    protected List<ListItemCategoryValues> doInBackground(Void... params) {
        LinkedHashMap<Measurement.Category, float[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, SKIP_EVERY_X_HOUR);
        List<ListItemCategoryValues> rowList = new ArrayList<>();
        for (Map.Entry<Measurement.Category, float[]> mapEntry : values.entrySet()) {
            rowList.add(new ListItemCategoryValues(mapEntry.getKey(), mapEntry.getValue()));
        }
        return rowList;
    }
}
