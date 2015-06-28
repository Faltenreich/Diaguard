package com.faltenreich.diaguard.database;

import android.util.Log;

import com.faltenreich.diaguard.DiaguardApplication;
import com.faltenreich.diaguard.database.measurements.BloodSugar;
import com.faltenreich.diaguard.database.measurements.Measurement;
import com.faltenreich.diaguard.helpers.Helper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTableConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Filip on 28.06.2015.
 */
public class DatabaseFacade {

    private static DatabaseFacade instance;
    private DatabaseHelper databaseHelper;

    public static DatabaseFacade getInstance() {
        if (DatabaseFacade.instance == null) {
            DatabaseFacade.instance = new DatabaseFacade();
        }
        return DatabaseFacade.instance;
    }

    private DatabaseFacade() {
        this.databaseHelper = OpenHelperManager.getHelper(DiaguardApplication.getContext(), DatabaseHelper.class);
    }

    public <T extends Model> Dao<T, Long> getDao(Class<T> clazz) {
        try {
            return databaseHelper.getDao(clazz);
        } catch (SQLException exception) {
            Log.e("DatabaseFacade", exception.getMessage());
            return null;
        }
    }

    public <T extends Model, D extends Model> QueryBuilder<T, ?> join(Class<T> clazzOne, Class<D> classTwo) throws SQLException {
        QueryBuilder<T, ?> qbOne = getDao(clazzOne).queryBuilder();
        QueryBuilder<D, ?> qbTwo = getDao(classTwo).queryBuilder();
        return qbOne.join(qbTwo);
    }

    public <T extends Measurement> long avg(Class<T> clazz, String avgColumn, Interval interval) throws SQLException {
        String classNameEntry = DatabaseTableConfig.extractTableName(Entry.class);
        String classNameMeasurement = DatabaseTableConfig.extractTableName(clazz);
        DateTimeFormatter format = Helper.getDateDatabaseFormat();
        DateTime intervalStart = interval.getStart().withTimeAtStartOfDay();
        DateTime intervalEnd = interval.getEnd().withTime(
                DateTimeConstants.HOURS_PER_DAY - 1,
                DateTimeConstants.MINUTES_PER_HOUR - 1,
                DateTimeConstants.SECONDS_PER_MINUTE - 1,
                DateTimeConstants.MILLIS_PER_SECOND - 1);
        String query =
                "SELECT AVG(" + avgColumn + ") FROM " + classNameMeasurement +
                " INNER JOIN " + classNameEntry +
                " ON " + classNameMeasurement + "." + Measurement.ENTRY_ID +
                " = " + classNameEntry + "." + Model.ID +
                " AND " + classNameEntry + "." + Entry.DATE +
                " >= Datetime('" + format.print(intervalStart) + "')" +
                " AND " + classNameEntry + "." + Entry.DATE +
                " <= Datetime('" + format.print(intervalEnd) + "');";
        GenericRawResults<String[]> rawResults = getDao(clazz).queryRaw(query);
        List<String[]> results = rawResults.getResults();
        String[] resultArray = results.get(0);
        String result = resultArray[0];
        return result != null ? Long.parseLong(result) : 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends Measurement> T getEntryWithMeasurement(Class<T> clazz) throws SQLException {
        QueryBuilder joinQb = join(Entry.class, BloodSugar.class).orderBy(Entry.DATE, false).limit(1L);
        return (T) joinQb.query().get(0);
    }
}
