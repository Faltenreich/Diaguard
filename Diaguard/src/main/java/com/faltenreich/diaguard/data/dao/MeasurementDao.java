package com.faltenreich.diaguard.data.dao;

import android.util.Log;

import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Faltenreich on 06.09.2015.
 */
public class MeasurementDao <M extends Measurement> extends BaseDao<M> {

    private static final String TAG = MeasurementDao.class.getSimpleName();

    private static HashMap<Class, MeasurementDao> instances;

    public static <M extends Measurement> MeasurementDao getInstance(Class<M> clazz) {
        if (instances == null) {
            instances = new HashMap<>();
        }
        MeasurementDao measurementDao = instances.get(clazz);
        if (measurementDao == null) {
            instances.put(clazz, new MeasurementDao<>(clazz));
            measurementDao = instances.get(clazz);
        }
        return measurementDao;
    }

    private MeasurementDao(Class<M> clazz) {
        super(clazz);
    }

    public float avg(String avgColumn, DateTime dateTime) {
        return avg(avgColumn, new Interval(dateTime, dateTime));
    }

    public float avg(String avgColumn, Interval interval) {
        String classNameEntry = DatabaseTableConfig.extractTableName(Entry.class);
        String classNameMeasurement = DatabaseTableConfig.extractTableName(getClazz());
        long intervalStart = interval.getStart().withTimeAtStartOfDay().getMillis();
        long intervalEnd = interval.getEnd().withTime(
                DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1)
                .getMillis();
        String query = "SELECT AVG(" + avgColumn + ")" +
                " FROM " + classNameMeasurement +
                " INNER JOIN " + classNameEntry +
                " ON " + classNameMeasurement + "." + Measurement.Column.ENTRY +
                " = " + classNameEntry + "." + BaseEntity.Column.ID +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " >= " + intervalStart +
                " AND " + classNameEntry + "." + Entry.Column.DATE +
                " <= " + intervalEnd + ";";

        List<String[]> results;
        try {
            GenericRawResults<String[]> rawResults = getDao().queryRaw(query);
            results = rawResults.getResults();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not calculate avg of %s", getClazz().getSimpleName()));
            return 0;
        }

        String[] resultArray = results.get(0);
        String result = resultArray[0];
        try {
            return Float.parseFloat(result);
        } catch (NumberFormatException exception) {
            return 0;
        } catch (NullPointerException exception) {
            return 0;
        }
    }

    public List<M> getMeasurements(Entry entry) {
        try {
            return getDao().queryBuilder().where().eq(Measurement.Column.ENTRY, entry).query();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not fetch measurements of category '%s'", getClazz().toString()));
            return new ArrayList<>();
        }
    }

    public M getMeasurement(Entry entry) {
        try {
            return getDao().queryBuilder().where().eq(Measurement.Column.ENTRY, entry).queryForFirst();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not fetch measurement of category '%s'", getClazz().toString()));
            return null;
        }
    }

    public int deleteMeasurements(Entry entry) {
        try {
            DeleteBuilder deleteBuilder = getDao().deleteBuilder();
            deleteBuilder.where().eq(Measurement.Column.ENTRY, entry);
            return deleteBuilder.delete();
        } catch (SQLException exception) {
            Log.e(TAG, String.format("Could not delete '%s' measurements of entry with id %d", getClazz().toString(), entry.getId()));
            return 0;
        }
    }
}
