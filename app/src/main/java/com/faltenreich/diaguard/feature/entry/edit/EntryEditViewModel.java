package com.faltenreich.diaguard.feature.entry.edit;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.data.async.DataLoader;
import com.faltenreich.diaguard.shared.data.async.DataLoaderListener;
import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.FoodDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.Consumer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

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

    String getAlarmInMinutesAsText(Context context) {
        return alarmInMinutes > 0 ?
            String.format("%s %s",
                context.getString(R.string.alarm_reminder_in),
                DateTimeUtils.parseInterval(context, alarmInMinutes * DateTimeConstants.MILLIS_PER_MINUTE)) :
            context.getString(R.string.alarm_reminder_none);
    }

    void setAlarmInMinutes(int alarmInMinutes) {
        this.alarmInMinutes = alarmInMinutes;
    }

    boolean isEditing() {
        return entryId > 0;
    }

    void setArguments(@Nullable Bundle arguments) {
        if (arguments != null) {
            entryId = arguments.getLong(EXTRA_ENTRY_ID);
            foodId = arguments.getLong(EXTRA_FOOD_ID);
            dateTime = arguments.get(EXTRA_DATE) != null
                ? (DateTime) arguments.getSerializable(EXTRA_DATE)
                : null;
        }
        if (dateTime == null) {
            dateTime = DateTime.now();
        }
    }

    void observeEntry(Context context, Consumer<Entry> callback) {
        if (entry != null) {
            callback.accept(entry);
        } else if (entryId > 0) {
            fetchEntry(context, callback);
        } else if (foodId > 0) {
            fetchFood(context, callback);
        } else {
            entry = new Entry();
            entry.setDate(dateTime);
            callback.accept(entry);
        }
    }

    private void fetchEntry(Context context, Consumer<Entry> callback) {
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
                callback.accept(entry);
            }
        });
    }

    private void fetchFood(Context context, Consumer<Entry> callback) {
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
                callback.accept(entry);
            }
        });
    }

    void observeTags(Context context, Consumer<List<Tag>> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad() {
                return TagDao.getInstance().getRecent();
            }

            @Override
            public void onDidLoad(List<Tag> tags) {
                callback.accept(tags);
            }
        });
    }

    Category[] getActiveCategories() {
        return PreferenceStore.getInstance().getActiveCategories();
    }

    Category[] getPinnedCategory() {
        return PreferenceStore.getInstance().getPinnedCategories();
    }
}
