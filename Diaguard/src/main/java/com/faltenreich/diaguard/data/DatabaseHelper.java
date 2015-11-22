package com.faltenreich.diaguard.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.dao.MeasurementDao;
import com.faltenreich.diaguard.data.entity.Activity;
import com.faltenreich.diaguard.data.entity.BaseEntity;
import com.faltenreich.diaguard.data.entity.BloodSugar;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.data.entity.HbA1c;
import com.faltenreich.diaguard.data.entity.Insulin;
import com.faltenreich.diaguard.data.entity.Meal;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.entity.Pressure;
import com.faltenreich.diaguard.data.entity.Pulse;
import com.faltenreich.diaguard.data.entity.Weight;
import com.faltenreich.diaguard.util.Helper;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;

import org.joda.time.format.DateTimeFormat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // Metadata
    private static final String DATABASE_NAME = "diaguard.db";

    public static final int DATABASE_VERSION_1_0 = 17;
    public static final int DATABASE_VERSION_1_1 = 18;
    public static final int DATABASE_VERSION_1_3 = 19;

    public static final Class[] tables = new Class[] {
            Entry.class,
            BloodSugar.class,
            Insulin.class,
            Meal.class,
            Activity.class,
            HbA1c.class,
            Weight.class,
            Pulse.class,
            Pressure.class
    };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, getVersion());
    }

    public static int getVersion() {
        return DATABASE_VERSION_1_3;
    }

    public static <T extends BaseEntity> String getTableName(Class<T> clazz) {
        return DatabaseTableConfig.extractTableName(clazz);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        for (Class tableClass : tables) {
            try {
                TableUtils.createTable(connectionSource, tableClass);
            }
            catch (SQLException exception) {
                Log.e(DatabaseHelper.class.getName(), "Couldn't create table " + tableClass.getSimpleName(), exception);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            onCreate(sqLiteDatabase);
            int upgradeFromVersion = oldVersion;
            while (upgradeFromVersion < newVersion) {
                switch (upgradeFromVersion) {
                    case DATABASE_VERSION_1_0:
                        upgradeToVersion18(sqLiteDatabase);
                        break;
                    case DATABASE_VERSION_1_1:
                        upgradeToVersion19(sqLiteDatabase, connectionSource);
                        break;
                }
                upgradeFromVersion++;
            }
        }
    }

    private <M extends Measurement> void upgradeToVersion19(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {

        List<Entry> entries = new ArrayList<>();
        Cursor cursor = sqliteDatabase.rawQuery("SELECT * FROM " + DatabaseHelper.ENTRY, null);
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Entry entry = new Entry();
                entry.setId(Long.parseLong(cursor.getString(0)));
                entry.setDate(DateTimeFormat.forPattern(DATE_TIME_FORMAT_1_1).parseDateTime(cursor.getString(1)));
                entry.setNote(cursor.getString(2));
                entries.add(entry);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Log.i(TAG, String.format("Got %d entries", entries.size()));

        HashMap<Entry, List<M>> entities = new HashMap<>();
        for (Entry entry : entries) {
            String query = String.format("SELECT * FROM %s WHERE %s = %d", DatabaseHelper.MEASUREMENT, ID, entry.getId());
            cursor = sqliteDatabase.rawQuery(query, null);

            List<M> measurements = new ArrayList<>();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    try {
                        Measurement.Category category = Helper.valueOf(Measurement.Category.class, cursor.getString(2));
                        M measurement = (M) category.toClass().newInstance();
                        float value = Float.parseFloat(cursor.getString(1));
                        float[] values = new float[measurement.getValues().length];
                        values[0] = value;
                        measurement.setValues(values);
                        measurements.add(measurement);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    cursor.moveToNext();
                }
            }
            entities.put(entry, measurements);
            cursor.close();
            Log.i(TAG, String.format("Got %d measurements", measurements.size()));
        }

        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + ENTRY);
        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + MEASUREMENT);
        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + FOOD);
        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + FOOD_EATEN);
        Log.i(TAG, "Cleared database %d measurements");

        onCreate(sqliteDatabase, connectionSource);
        Log.i(TAG, "Created updated database");

        for (Map.Entry<Entry, List<M>> mapEntry : entities.entrySet()) {
            Entry entry = EntryDao.getInstance().createOrUpdate(mapEntry.getKey());
            for (Measurement measurement : mapEntry.getValue()) {
                measurement.setEntry(entry);
                MeasurementDao.getInstance(measurement.getClass()).createOrUpdate(measurement);
            }
        }

        Log.i(TAG, "Finished upgrade to version 19");
    }

    // region Deprecated

    private static final String DATE_TIME_FORMAT_1_1 = "yyyy-MM-dd HH:mm:ss";

    private static final String ID = "_id";
    private static final String ENTRY = "entry";
    private static final String DATE = "date";
    private static final String NOTE = "note";
    private static final String VALUE = "value";
    private static final String ENTRY_ID = "entry_id";
    private static final String FOOD = "food";
    private static final String CARBOHYDRATES = "carbohydrates";
    private static final String NAME = "name";
    private static final String FOOD_ID = "food_id";
    private static final String EVENTS = "events";
    private static final String NOTES = "notes";
    private static final String MEASUREMENT = "measurement";
    private static final String CATEGORY = "category";
    private static final String MEASUREMENT_ID = "measurementId";
    private static final String FOOD_EATEN = "food_eaten";

    private void onCreateVersion17(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                EVENTS + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                VALUE + " REAL NOT NULL, " +
                DATE + " TEXT NOT NULL, " +
                NOTES + " TEXT, " +
                CATEGORY + " TEXT NOT NULL);");
    }

    private void onCreateVersion18(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                ENTRY + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT NOT NULL, " +
                NOTE + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                MEASUREMENT + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VALUE + " REAL NOT NULL, " +
                CATEGORY + " TEXT NOT NULL, " +
                ENTRY_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ENTRY_ID + ") REFERENCES " + ENTRY + " (" + ID + ") ON DELETE CASCADE);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                FOOD + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CARBOHYDRATES + " REAL NOT NULL, " +
                NAME + " TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                FOOD_EATEN + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEASUREMENT_ID + " INTEGER, " +
                FOOD_ID + " INTEGER, " +
                "FOREIGN KEY(" + MEASUREMENT_ID + ") REFERENCES " + MEASUREMENT + " (" + ID + ") ON UPDATE CASCADE, " +
                "FOREIGN KEY(" + FOOD_ID + ") REFERENCES " + FOOD + " (" + ID + ") ON DELETE CASCADE);");
    }

    private void upgradeToVersion18(SQLiteDatabase sqliteDatabase) {
        String query = "SELECT * FROM " + DatabaseHelper.EVENTS + " ORDER BY " + DatabaseHelper.DATE;
        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {

                // Entry
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.DATE, cursor.getString(2));
                values.put(DatabaseHelper.NOTE, cursor.getString(3));
                long entryId = sqliteDatabase.insertOrThrow(DatabaseHelper.ENTRY, null, values);

                // Measurement
                values = new ContentValues();
                values.put(DatabaseHelper.VALUE, Float.parseFloat(cursor.getString(1)));
                values.put(DatabaseHelper.CATEGORY, cursor.getString(4));
                values.put(DatabaseHelper.ENTRY_ID, entryId);
                sqliteDatabase.insertOrThrow(DatabaseHelper.MEASUREMENT, null, values);

                cursor.moveToNext();
            }
        }
        sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + EVENTS + ";");
    }

    // endregion
}