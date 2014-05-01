package com.android.diaguard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Filip on 20.10.13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Metadata
    private static final String DATABASE_NAME = "diaguard.db";
    private static final int DATABASE_VERSION = 17;

    // Primary key: Underscore for CursorAdapter to work
    public static final String ID = "_id";

    // Events
    public static final String EVENTS = "events";
    public static final String VALUE = "value";
    public static final String DATE = "date";
    public static final String NOTES = "notes";
    public static final String CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        sqliteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + EVENTS + " (" + ID + " INTEGER PRIMARY KEY, " + VALUE + " REAL NOT NULL, " +
                DATE + " TEXT NOT NULL, " + NOTES + " TEXT, " + CATEGORY + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int oldVersion, int newVersion) {
        // Alter to avoid dropping user data
        // if (newVersion != oldVersion)
        // sqliteDatabase.execSQL("DROP TABLE IF EXISTS " + EVENTS);
    }
}