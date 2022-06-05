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
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Food;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.shared.data.primitive.Consumer;
import com.faltenreich.diaguard.shared.data.reflect.ObjectFactory;
import com.faltenreich.diaguard.shared.data.repository.EntryTagRepository;
import com.faltenreich.diaguard.shared.data.repository.FoodRepository;
import com.faltenreich.diaguard.shared.data.repository.TagRepository;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class EntryEditViewModel {

    private long entryId;
    private long foodId;
    private DateTime dateTime;
    private Category category;

    private Entry entry;
    private List<EntryTag> entryTags;
    private int alarmInMinutes;

    protected EntryEditViewModel() {
        this.entryTags = new ArrayList<>();
    }

    Entry getEntry() {
        return entry;
    }

    List<Measurement> getMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        for (Measurement measurement : entry.getMeasurementCache()) {
            if (measurement.hasValue()) {
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    List<EntryTag> getEntryTags() {
        return entryTags;
    }

    int getIndexOfTag(Tag tag) {
        for (int index = 0; index < entryTags.size(); index++) {
            EntryTag entryTag = entryTags.get(index);
            if (entryTag.getTag() != null && entryTag.getTag().equals(tag)) {
                return index;
            }
        }
        return -1;
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

    Category[] getActiveCategories() {
        return PreferenceStore.getInstance().getActiveCategories();
    }

    Category[] getPinnedCategories() {
        return PreferenceStore.getInstance().getPinnedCategories();
    }

    void setArguments(@Nullable Bundle arguments) {
        entryId = arguments != null
            ? arguments.getLong(EntryEditFragment.EXTRA_ENTRY_ID, -1L)
            : -1L;
        foodId = arguments != null
            ? arguments.getLong(EntryEditFragment.EXTRA_FOOD_ID, -1L)
            : -1L;
        dateTime = arguments != null && arguments.get(EntryEditFragment.EXTRA_DATE) != null
            ? (DateTime) arguments.getSerializable(EntryEditFragment.EXTRA_DATE)
            : DateTime.now();
        category = arguments != null && arguments.get(EntryEditFragment.EXTRA_CATEGORY) != null
            ? (Category) arguments.get(EntryEditFragment.EXTRA_CATEGORY)
            : null;
    }

    void observeEntry(Context context, Consumer<Entry> callback) {
        if (entry != null) {
            callback.accept(entry);
        } else if (entryId != -1L) {
            fetchEntry(context, entryId, callback);
        } else if (foodId != -1L) {
            fetchFood(context, foodId, callback);
        } else {
            createEntry();
            callback.accept(entry);
        }
    }

    private void createEntry() {
        entry = new Entry();
        entry.setDate(dateTime);

        if (category != null) {
            Measurement measurement = ObjectFactory.createFromClass(category.toClass());
            measurement.setEntry(entry);
            entry.getMeasurementCache().add(measurement);
        }
    }

    private void fetchEntry(Context context, final long entryId, Consumer<Entry> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<Entry>() {
            @Override
            public Entry onShouldLoad(Context context) {
                EntryDao dao = EntryDao.getInstance();
                Entry entry = dao.getById(entryId);
                entry.setMeasurementCache(dao.getMeasurements(entry));
                return entry;
            }
            @Override
            public void onDidLoad(Entry result) {
                entry = result;
                entryTags = EntryTagRepository.getInstance().getByEntry(entry);
                callback.accept(entry);
            }
        });
    }

    private void fetchFood(Context context, final long foodId, Consumer<Entry> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<Food>() {
            @Override
            public Food onShouldLoad(Context context) {
                return FoodRepository.getInstance().getById(foodId);
            }
            @Override
            public void onDidLoad(Food food) {
                entry = new Entry();
                entry.setDate(dateTime);

                Meal meal = new Meal();
                meal.setEntry(entry);
                entry.getMeasurementCache().add(meal);

                FoodEaten foodEaten = new FoodEaten();
                foodEaten.setMeal(meal);
                foodEaten.setFood(food);
                foodEaten.setAmountInGrams(0);
                meal.getFoodEatenCache().add(foodEaten);

                callback.accept(entry);
            }
        });
    }

    void observeTags(Context context, Consumer<List<Tag>> callback) {
        DataLoader.getInstance().load(context, new DataLoaderListener<List<Tag>>() {
            @Override
            public List<Tag> onShouldLoad(Context context) {
                return TagRepository.getInstance().getAll();
            }

            @Override
            public void onDidLoad(List<Tag> tags) {
                callback.accept(tags);
            }
        });
    }
}
