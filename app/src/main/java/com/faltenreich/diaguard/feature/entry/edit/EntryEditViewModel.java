package com.faltenreich.diaguard.feature.entry.edit;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import org.joda.time.DateTime;

import java.util.List;

class EntryEditViewModel {

    static final String EXTRA_ENTRY_ID = "entryId";
    static final String EXTRA_FOOD_ID = "foodId";
    static final String EXTRA_DATE = "date";

    private long entryId;
    private long foodId;
    private DateTime dateTime;

    private Entry entry;
    private List<EntryTag> entryTags;
    private int alarmInMinutes;

    Entry getEntry() {
        return entry;
    }

    List<EntryTag> getEntryTags() {
        return entryTags;
    }

    int getAlarmInMinutes() {
        return alarmInMinutes;
    }

    void setAlarmInMinutes(int alarmInMinutes) {
        this.alarmInMinutes = alarmInMinutes;
    }

    void extractArgumentsFromBundle(@Nullable Bundle bundle) {
        if (bundle != null) {
            entryId = bundle.getLong(EXTRA_ENTRY_ID);
            foodId = bundle.getLong(EXTRA_FOOD_ID);
            dateTime = bundle.get(EXTRA_DATE) != null
                ? (DateTime) bundle.getSerializable(EXTRA_DATE)
                : DateTime.now();
        }
    }

    boolean isEditing() {
        return entryId > 0;
    }

    void observeEntry(Context context, Callback<Entry> callback) {
        if (entry != null) {
            callback.onObserve(entry);
        } else if (entryId > 0) {
            fetchEntry(context, callback);
        } else if (foodId > 0) {
            fetchFood(context, callback);
        } else {
            entry = new Entry();
            entry.setDate(dateTime);
            callback.onObserve(entry);
        }
    }

    private void fetchEntry(Context context, Callback<Entry> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                EntryDao dao = EntryDao.getInstance();
                entry = dao.getById(entryId);
                if (entry != null) {
                    entry.setMeasurementCache(dao.getMeasurements(entry));
                    entryTags = EntryTagDao.getInstance().getAll(entry);
                }
                return null;
            }
            @Override
            public void onDidLoad(List<Tag> data) {
                callback.onObserve(entry);
            }
        });
    }

    private void fetchFood(Context context, Callback<Entry> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<Food>() {
            @Override
            public Food onShouldLoad() {
                return FoodDao.getInstance().getById(foodId);
            }

            @Override
            public void onDidLoad(Food food) {
                entry = new Entry();
                entry.setDate(dateTime);
                // TODO: Add food to entry
                callback.onObserve(entry);
            }
        });
    }

    void observeTags(Context context, Callback<List<Tag>> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                return TagDao.getInstance().getRecent();
            }

            @Override
            public void onDidLoad(List<Tag> tags) {
                callback.onObserve(tags);
            }
        });
    }

    interface Callback<T> {
        void onObserve(T data);
    }
}
