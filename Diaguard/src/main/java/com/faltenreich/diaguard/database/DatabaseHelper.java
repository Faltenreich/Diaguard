package com.faltenreich.diaguard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Metadata
    private static final String DATABASE_NAME = "diaguard.db";
    private static final int DATABASE_VERSION = 18;
    private static final int DATABASE_VERSION_1_0 = 17;

    // Primary key: Underscore for CursorAdapter to work
    public static final String ID = "_id";

    // Events
    public static final String EVENTS = "events";
    public static final String VALUE = "value";
    public static final String DATE = "date";
    public static final String NOTES = "notes";
    public static final String CATEGORY = "category";

    // Food
    public static final String FOOD = "food";
    public static final String CARBOHYDRATES = "carbohydrates";
    public static final String NAME = "name";
    public static final String EVENT_ID = "eventId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                EVENTS + " (" + ID + " INTEGER PRIMARY KEY, " +
                VALUE + " REAL NOT NULL, " +
                DATE + " TEXT NOT NULL, " +
                NOTES + " TEXT, " +
                CATEGORY + " TEXT NOT NULL);");
        sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " +
                FOOD + " (" + ID + " INTEGER PRIMARY KEY, " +
                CARBOHYDRATES + " REAL NOT NULL, " +
                NAME + " TEXT, " +
                DATE + " TEXT NOT NULL, " +
                EVENT_ID + " INTEGER, " +
                "FOREIGN KEY(" + EVENT_ID + ") REFERENCES " + EVENTS + " ( " + ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            onCreate(sqliteDatabase);

            if (oldVersion == DATABASE_VERSION_1_0) {
            }
        }
    }
}