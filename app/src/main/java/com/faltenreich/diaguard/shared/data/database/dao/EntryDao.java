package com.faltenreich.diaguard.shared.data.database.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.faltenreich.diaguard.feature.preference.data.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.FoodEaten;
import com.faltenreich.diaguard.shared.data.database.entity.Insulin;
import com.faltenreich.diaguard.shared.data.database.entity.Meal;
import com.faltenreich.diaguard.shared.data.database.entity.Measurement;
import com.faltenreich.diaguard.shared.data.database.entity.Pressure;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;
import com.faltenreich.diaguard.feature.timeline.day.table.CategoryListItemUtils;
import com.faltenreich.diaguard.shared.data.primitive.ArrayUtils;
import com.faltenreich.diaguard.feature.category.CategoryComparatorFactory;
import com.faltenreich.diaguard.feature.timeline.day.table.CategoryValueListItem;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Faltenreich on 05.09.2015.
 */
public class EntryDao extends BaseDao<Entry> {

    private static final String TAG = EntryDao.class.getSimpleName();

    private static EntryDao instance;

    public static EntryDao getInstance() {
        if (instance == null) {
            instance = new EntryDao();
        }
        return instance;
    }

    private EntryDao() {
        super(Entry.class);
    }

    @Override
    public int delete(Entry entry) {
        for (Measurement measurement : getMeasurements(entry)) {
            entry.getMeasurementCache().add(measurement);
            MeasurementDao.getInstance(measurement.getClass()).delete(measurement);
        }
        EntryTagDao.getInstance().delete(EntryTagDao.getInstance().getAll(entry));
        return super.delete(entry);
    }

    public List<Entry> getEntriesOfDay(DateTime day) {
        return getEntriesBetween(day, day);
    }

    public List<Entry> getEntriesBetween(DateTime start, DateTime end) {
        if (start == null || end == null) {
            return new ArrayList<>();
        }
        start = start.withTimeAtStartOfDay();
        end = end.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
            DateTimeConstants.MINUTES_PER_HOUR - 1,
            DateTimeConstants.SECONDS_PER_MINUTE - 1,
            DateTimeConstants.MILLIS_PER_SECOND - 1);
        try {
            return getQueryBuilder()
                .orderBy(Entry.Column.DATE, true)
                .where().gt(Entry.Column.DATE, start)
                .and().lt(Entry.Column.DATE, end)
                .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Measurement> getMeasurements(Entry entry) {
        return getMeasurements(entry, PreferenceHelper.getInstance().getActiveCategories());
    }

    public List<Measurement> getMeasurements(Entry entry, Category[] categories) {
        List<Measurement> measurements = new ArrayList<>();
        for (Category category : categories) {
            Measurement measurement = MeasurementDao.getInstance(category.toClass()).getMeasurement(entry);
            if (measurement != null) {
                measurements.add(measurement);
            }
        }
        Collections.sort(measurements, CategoryComparatorFactory.getInstance().createComparatorFromMeasurements());
        return measurements;
    }

    private <M extends Measurement> QueryBuilder<Entry, Long> join(Class<M> clazz) {
        QueryBuilder<Entry, Long> qbOne = getQueryBuilder();
        QueryBuilder<M, Long> qbTwo = MeasurementDao.getInstance(clazz).getQueryBuilder();
        try {
            return qbOne.join(qbTwo);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public <M extends Measurement> Entry getLatestWithMeasurement(Class<M> clazz) {
        try {
            return join(clazz).orderBy(Entry.Column.DATE, false).where().le(Entry.Column.DATE, DateTime.now()).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public <M extends Measurement> List<Entry> getAllWithMeasurementFromToday(Class<M> clazz) {
        try {
            return join(clazz).where().gt(Entry.Column.DATE, DateTime.now().withTimeAtStartOfDay()).query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * @return HashMap with non-null but zeroed and default values for given categories and time periods
     */
    public LinkedHashMap<Category, CategoryValueListItem[]> getAverageDataTable(DateTime day, Category[] categories, int hoursToSkip) {
        if (day == null) {
            return new LinkedHashMap<>();
        }

        int indices = DateTimeConstants.HOURS_PER_DAY / hoursToSkip;

        // Key: Category, Value: Fixed-size array of values per hour-index
        LinkedHashMap<Category, CategoryValueListItem[]> values = new LinkedHashMap<>();
        for (Category category : categories) {
            values.put(category, new CategoryValueListItem[indices]);
        }

        for (Category category : categories) {
            // Key: Hour-index, Value: Values of hour-index
            LinkedHashMap<Integer, List<CategoryValueListItem>> valuesOfHours = new LinkedHashMap<>();
            for (int index = 0; index < indices; index++) {
                valuesOfHours.put(index, new ArrayList<>());
            }

            List<Measurement> measurements = MeasurementDao.getInstance(category.toClass()).getMeasurements(day);
            for (Measurement measurement : measurements) {
                int index = measurement.getEntry().getDate().hourOfDay().get() / hoursToSkip;
                CategoryValueListItem item = new CategoryValueListItem(category);

                switch (category) {
                    case INSULIN:
                        Insulin insulin = (Insulin) measurement;
                        item.setValueOne(insulin.getBolus());
                        item.setValueTwo(insulin.getCorrection());
                        item.setValueThree(insulin.getBasal());
                        break;
                    case PRESSURE:
                        Pressure pressure = (Pressure) measurement;
                        item.setValueOne(pressure.getSystolic());
                        item.setValueTwo(pressure.getDiastolic());
                        break;
                    default:
                        float value = category.stackValues() ? ArrayUtils.sum(measurement.getValues()) : ArrayUtils.avg(measurement.getValues());
                        if (category == Category.MEAL) {
                            for (FoodEaten foodEaten : ((Meal) measurement).getFoodEaten()) {
                                value += foodEaten.getCarbohydrates();
                            }
                        }
                        item.setValueOne(value);
                        break;
                }

                List<CategoryValueListItem> valuesOfHour = valuesOfHours.get(index);
                if (valuesOfHour == null) {
                    valuesOfHours.put(index, new ArrayList<>());
                }
                valuesOfHours.get(index).add(item);
            }

            // Average for old values
            for (int index = 0; index < indices; index++) {
                List<CategoryValueListItem> valuesOfHour = valuesOfHours.get(index);
                CategoryValueListItem value = category.stackValues() ?
                    CategoryListItemUtils.sum(category, valuesOfHour) :
                    CategoryListItemUtils.avg(category, valuesOfHour);
                values.get(category)[index] = value;
            }
        }
        return values;
    }

    public long count(Category category, DateTime start, DateTime end) {
        try {
            QueryBuilder<Entry, Long> queryBuilder = join(category.toClass());
            if (queryBuilder != null) {
                return queryBuilder
                    .where().ge(Entry.Column.DATE, start)
                    .and().le(Entry.Column.DATE, end)
                    .countOf();
            } else {
                return -1;
            }
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countBelow(DateTime start, DateTime end, float maximumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<Measurement, Long> queryBuilderMeasurement = MeasurementDao.getInstance(BloodSugar.class).getQueryBuilder();
            queryBuilderMeasurement.where().lt(BloodSugar.Column.MGDL, maximumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countAbove(DateTime start, DateTime end, float minimumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<Measurement, Long> queryBuilderMeasurement = MeasurementDao.getInstance(BloodSugar.class).getQueryBuilder();
            queryBuilderMeasurement.where().gt(BloodSugar.Column.MGDL, minimumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countBetween(DateTime start, DateTime end, float minimumValue, float maximumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getQueryBuilder();
            queryBuilderEntry
                .where().ge(Entry.Column.DATE, start)
                .and().le(Entry.Column.DATE, end);

            QueryBuilder<Measurement, Long> queryBuilderMeasurement = MeasurementDao.getInstance(BloodSugar.class).getQueryBuilder();
            queryBuilderMeasurement
                .where().ge(BloodSugar.Column.MGDL, minimumValue)
                .and().le(BloodSugar.Column.MGDL, maximumValue);

            return queryBuilderEntry.join(queryBuilderMeasurement).countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    @NonNull
    public List<Entry> search(@NonNull String query, int page, int pageSize) {
        try {
            query = "%" + query + "%";
            QueryBuilder<Tag, Long> tagQb = TagDao.getInstance().getQueryBuilder();
            tagQb.where().like(Tag.Column.NAME, new SelectArg(query));
            QueryBuilder<EntryTag, Long> entryTagQb = EntryTagDao.getInstance().getQueryBuilder().leftJoinOr(tagQb);
            QueryBuilder<Entry, Long> entryQb = getQueryBuilder().leftJoinOr(entryTagQb)
                .offset((long) (page * pageSize))
                .limit((long) pageSize)
                .orderBy(Entry.Column.DATE, false);
            entryQb.where().like(Entry.Column.NOTE, new SelectArg(query));
            return entryQb.distinct().query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }
}