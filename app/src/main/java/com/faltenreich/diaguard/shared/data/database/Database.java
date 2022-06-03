package com.faltenreich.diaguard.shared.data.database;

import android.content.Context;

import androidx.room.Room;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class Database {

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
        roomDatabase = Room
            .databaseBuilder(context, RoomDatabase.class, RoomDatabase.DATABASE_NAME)
            .allowMainThreadQueries() // TODO: Remove when migration has been completed
            .build();
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
