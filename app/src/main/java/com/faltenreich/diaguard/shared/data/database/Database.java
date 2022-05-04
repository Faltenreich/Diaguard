package com.faltenreich.diaguard.shared.data.database;

import android.content.Context;

import androidx.room.Room;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class Database {

    public static final String DATABASE_NAME = "diaguard.db";
    public static final int DATABASE_VERSION = DatabaseHelper.DATABASE_VERSION_3_1;

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private DatabaseHelper databaseHelper;
    private RoomDatabase roomDatabase;

    private Database() {}

    public void init(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        roomDatabase = Room.databaseBuilder(context, RoomDatabase.class, DATABASE_NAME).build();
    }

    public void close() {
        OpenHelperManager.releaseHelper();
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public RoomDatabase getDatabase() {
        return roomDatabase;
    }
}
