package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.util.ArrayUtils;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Entry> getEntriesOfDay(DateTime day) {
        return getEntriesBetween(day, day);
    }

    public List<Entry> getEntriesBetween(DateTime start, DateTime end) {
        start = start.withTimeAtStartOfDay();
        end = end.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1);
        try {
            return getDao().queryBuilder().orderBy(Entry.Column.DATE, true).where().gt(Entry.Column.DATE, start).and().lt(Entry.Column.DATE, end).query();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Measurement> getMeasurements(Entry entry) {
        return getMeasurements(entry, PreferenceHelper.getInstance().getActiveCategories());
    }

    public List<Measurement> getMeasurements(Entry entry, Measurement.Category[] categories) {
        List<Measurement> measurements = new ArrayList<>();
        for (Measurement.Category category : categories) {
            Measurement measurement = MeasurementDao.getInstance(category.toClass()).getMeasurement(entry);
            if (measurement != null) {
                measurements.add(measurement);
            }
        }
        return measurements;
    }

    private <M extends Measurement> QueryBuilder<Entry, Long> join(Class<M> clazz) {
        QueryBuilder<Entry, Long> qbOne = getDao().queryBuilder();
        QueryBuilder<M, Long> qbTwo = MeasurementDao.getInstance(clazz).getDao().queryBuilder();
        try {
            return qbOne.join(qbTwo);
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return null;
        }
    }

    public <M extends Measurement> Entry getLatestWithMeasurement(Class<M> clazz) {
        try {
            return join(clazz).orderBy(Entry.Column.DATE, false).queryForFirst();
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

    public LinkedHashMap<Measurement.Category, float[]> getAverageDataTable(DateTime day, Measurement.Category[] categories, int hoursToSkip) {
        LinkedHashMap<Measurement.Category, float[]> values = new LinkedHashMap<>();
        for (Measurement.Category category : categories) {
            values.put(category, new float[DateTimeConstants.HOURS_PER_DAY / hoursToSkip]);
        }
        for(Entry entry : getEntriesOfDay(day)) {
            for (Measurement measurement : getMeasurements(entry, categories)) {
                Measurement.Category category = measurement.getCategory();
                boolean valueIsAverage =
                        category == Measurement.Category.BLOODSUGAR ||
                                category == Measurement.Category.HBA1C ||
                                category == Measurement.Category.WEIGHT ||
                                category == Measurement.Category.PULSE ||
                                category == Measurement.Category.PRESSURE;
                int index = measurement.getEntry().getDate().hourOfDay().get() / hoursToSkip;
                boolean valueIsSum = category != Measurement.Category.PRESSURE;
                float value = valueIsSum ? ArrayUtils.sum(measurement.getValues()) : ArrayUtils.avg(measurement.getValues());
                float oldValue = values.get(category)[index];
                // TODO: Divisor is not 2 but count
                float newValue = valueIsAverage ?
                        oldValue > 0 ?
                                (oldValue + value) / 2 :
                                value :
                        oldValue + value;
                values.get(category)[index] = newValue;
            }
        }
        return values;
    }

    public long count(Measurement.Category category, DateTime start, DateTime end) {
        try {
            return join(category.toClass())
                    .where().ge(Entry.Column.DATE, start)
                    .and().le(Entry.Column.DATE, end)
                    .countOf();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return -1;
        }
    }

    public long countBelow(DateTime start, DateTime end, float maximumValue) {
        try {
            QueryBuilder<Entry, Long> queryBuilderEntry = getDao().queryBuilder();
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
            QueryBuilder<Entry, Long> queryBuilderEntry = getDao().queryBuilder();
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
            QueryBuilder<Entry, Long> queryBuilderEntry = getDao().queryBuilder();
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

    public List<Entry> getAllWithNotes(DateTime day) {
        try {
            return getDao().queryBuilder()
                    .orderBy(Entry.Column.DATE, true)
                    .where().isNotNull(Entry.Column.NOTE)
                    .and().ge(Entry.Column.DATE, day.withTimeAtStartOfDay())
                    .and().le(Entry.Column.DATE, day.withTime(DateTimeConstants.HOURS_PER_DAY - 1,
                            DateTimeConstants.MINUTES_PER_HOUR - 1,
                            DateTimeConstants.SECONDS_PER_MINUTE - 1,
                            DateTimeConstants.MILLIS_PER_SECOND - 1))
                    .query();
        } catch (SQLException exception) {
            Log.e(TAG, exception.getMessage());
            return new ArrayList<>();
        }
    }
}