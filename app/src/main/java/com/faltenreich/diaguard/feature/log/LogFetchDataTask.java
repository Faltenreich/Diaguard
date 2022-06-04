package com.faltenreich.diaguard.feature.log;


import android.os.AsyncTask;

import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagOrmLiteDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodEatenDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.feature.log.empty.LogEmptyListItem;
import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.feature.log.month.LogMonthListItem;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.EndlessAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class LogFetchDataTask extends AsyncTask<Void, Integer, List<LogListItem>> {

    private final DateTime startDate;
    private final boolean scrollingDown;
    private final boolean isInitializing;
    private final Listener<List<LogListItem>> listener;

    LogFetchDataTask(DateTime startDate, boolean scrollingDown, boolean isInitializing, Listener<List<LogListItem>> listener) {
        this.startDate = startDate;
        this.scrollingDown = scrollingDown;
        this.isInitializing = isInitializing;
        this.listener = listener;
    }

    @Override
    protected List<LogListItem> doInBackground(Void... params) {
        List<LogListItem> listItems = getListItems(startDate, scrollingDown);
        if (isInitializing) {
            boolean newIsScrollingDown = !scrollingDown;
            int index = newIsScrollingDown ? listItems.size() : 0;
            listItems.addAll(index, getListItems(newIsScrollingDown ? startDate.plusDays(1) : startDate.minusDays(1), newIsScrollingDown));
        }
        return listItems;
    }

    @Override
    protected void onPostExecute(List<LogListItem> result) {
        listener.onFinish(result, scrollingDown);
    }

    private List<LogListItem> getListItems(DateTime startDate, boolean scrollingDown) {
        List<LogListItem> listItems = new ArrayList<>();
        DateTime date = startDate;
        boolean loadMore = true;

        while (loadMore) {
            List<Entry> entries = EntryDao.getInstance().getEntriesOfDay(date);
            for (Entry entry : entries) {
                List<Measurement> measurements = EntryDao.getInstance().getMeasurements(entry);
                entry.setMeasurementCache(measurements);
            }

            if (entries.size() > 0) {
                LogEntryListItem firstListItemEntryOfDay = null;

                for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
                    Entry entry = entries.get(entryIndex);
                    List<EntryTag> entryTags = EntryTagOrmLiteDao.getInstance().getByEntry(entry);
                    List<FoodEaten> foodEaten = new ArrayList<>();
                    for (Measurement measurement : entry.getMeasurementCache()) {
                        if (measurement instanceof Meal) {
                            foodEaten.addAll(FoodEatenDao.getInstance().getAll((Meal) measurement));
                        }
                    }
                    LogEntryListItem listItemEntry = new LogEntryListItem(entry, entryTags, foodEaten);

                    if (entryIndex == 0) {
                        firstListItemEntryOfDay = listItemEntry;
                    }

                    listItemEntry.setFirstListItemEntryOfDay(firstListItemEntryOfDay);
                    listItems.add(scrollingDown ? listItems.size() : entryIndex, listItemEntry);
                }

            } else {
                listItems.add(scrollingDown ? listItems.size() : 0, new LogEmptyListItem(date));
            }

            boolean isFirstDayOfMonth = date.dayOfMonth().get() == 1;
            if (isFirstDayOfMonth) {
                listItems.add(scrollingDown ? listItems.size() - 1 : 0, new LogMonthListItem(date));
            }

            loadMore = listItems.size() < (EndlessAdapter.BULK_SIZE);
            date = scrollingDown ? date.plusDays(1) : date.minusDays(1);
        }

        return listItems;
    }

    interface Listener<T> {
        void onFinish(T result, boolean scrollingDown);
    }
}