package com.faltenreich.diaguard.shared.data.database;

import android.content.Context;

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

    private Database() {}

    public void init(Context context) {
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public void close() {
        OpenHelperManager.releaseHelper();
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
