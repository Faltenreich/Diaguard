package com.faltenreich.diaguard.util.thread;

import android.content.Context;

import com.faltenreich.diaguard.adapter.list.ListItemCategory;
import com.faltenreich.diaguard.adapter.list.ListItemCategoryValue;
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

public class TimelineTableTask extends BaseAsyncTask<Void, Void, List<ListItemCategory>> {

    private static final int SKIP_EVERY_X_HOUR = 2;

    private DateTime day;
    private Measurement.Category[] categories;

    public TimelineTableTask(Context context, DateTime day, Measurement.Category[] categories, OnAsyncProgressListener<List<ListItemCategory>> onAsyncProgressListener) {
        super(context, onAsyncProgressListener);
        this.day = day;
        this.categories = categories;
    }

    protected List<ListItemCategory> doInBackground(Void... params) {
        List<ListItemCategory> rowList = new ArrayList<>();

        LinkedHashMap<Measurement.Category, float[]> values = EntryDao.getInstance().getAverageDataTable(day, categories, SKIP_EVERY_X_HOUR);
        for (Map.Entry<Measurement.Category, float[]> mapEntry : values.entrySet()) {
            Measurement.Category category = mapEntry.getKey();
            rowList.add(new ListItemCategory(category));

            for (float value : mapEntry.getValue()) {
                rowList.add(new ListItemCategoryValue(category, value));
            }
        }

        return rowList;
    }
}
