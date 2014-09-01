package com.faltenreich.diaguard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Metadata
    private static final String DATABASE_NAME = "diaguard.db";
    private static final int DATABASE_VERSION = 18;

    // For compatibility purposes
    private static final int DATABASE_VERSION_1_0 = 17;

    // Primary key: Underscore for CursorAdapter to work
    public static final String ID = "_id";

    // Entry
    public static final String ENTRY = "entry";
    public static final String DATE = "date";
    public static final String NOTE = "note";

    // Measurement
    public static final String MEASUREMENT = "measurement";
    public static final String VALUE = "value";
    public static final String CATEGORY = "category";
    public static final String ENTRY_ID = "entryId";

    // Food
    public static final String FOOD = "food";
    public static final String CARBOHYDRATES = "carbohydrates";
    public static final String NAME = "name";

    // Measurement_Food
    public static final String FOOD_EATEN = "food_eaten";
    public static final String MEASUREMENT_ID = "measurementId";
    public static final String FOOD_ID = "foodId";

    // DEPRECATED
    public static final String EVENTS = "entries";
    public static final String NOTES = "notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onCreateVersion18(sqLiteDatabase);
    }

    private void onCreateVersion17(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                EVENTS + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                VALUE + " REAL NOT NULL, " +
                DATE + " TEXT NOT NULL, " +
                NOTES + " TEXT, " +
                CATEGORY + " TEXT NOT NULL);");
    }

    private void insertTestDataVersion17(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO " + EVENTS + " (" +
                 VALUE + "," + DATE + "," + NOTES + "," + CATEGORY +
                ") VALUES (133.0,'2014-08-09 21:31:23','TestNote','BloodSugar');");
        sqLiteDatabase.execSQL("INSERT INTO " + EVENTS + " (" +
                VALUE + "," + DATE + "," + NOTES + "," + CATEGORY +
                ") VALUES (5,'2014-06-09 21:31:23','Notenoteyos','Meal');");
    }

    private void onCreateVersion18(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                ENTRY + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                DATE + " TEXT NOT NULL, " +
                NOTE + " TEXT);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                MEASUREMENT + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                VALUE + " REAL NOT NULL, " +
                CATEGORY + " TEXT NOT NULL, " +
                ENTRY_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ENTRY_ID + ") REFERENCES " + ENTRY + " (" + ID + ") ON UPDATE CASCADE);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                FOOD + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                CARBOHYDRATES + " REAL NOT NULL, " +
                NAME + " TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                FOOD_EATEN + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                MEASUREMENT_ID + " INTEGER, " +
                FOOD_ID + " INTEGER, " +
                "FOREIGN KEY(" + MEASUREMENT_ID + ") REFERENCES " + MEASUREMENT + " (" + ID + ") ON UPDATE CASCADE, " +
                "FOREIGN KEY(" + FOOD_ID + ") REFERENCES " + FOOD + " (" + ID + ") ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            onCreate(sqLiteDatabase);
            int upgradeFromVersion = oldVersion;
            while (upgradeFromVersion < newVersion) {
                switch (upgradeFromVersion) {
                    // Add Food, Entry and replace Event with Measurement
                    case DATABASE_VERSION_1_0:
                        upgradeToVersion18(sqLiteDatabase);
                        break;
                }
                upgradeFromVersion++;
            }
        }
    }

    // TODO: Group by Events with the same date
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
}