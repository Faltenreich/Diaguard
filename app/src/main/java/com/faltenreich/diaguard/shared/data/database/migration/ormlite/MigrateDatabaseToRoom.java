package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class MigrateDatabaseToRoom {

    private static final String TAG = MigrateDatabaseToRoom.class.getSimpleName();

    public MigrateDatabaseToRoom() {

    }

    public void migrate(Context context) {
        Log.d(TAG, "Migrating database from OrmLite to Room");
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        SQLiteDatabase ormLiteDatabase = databaseHelper.getReadableDatabase();
        new MigrateTagsToRoom().migrate(ormLiteDatabase);
        new MigrateEntryTagsToRoom().migrate(ormLiteDatabase);
        // TODO: Maybe delete ormLiteDatabase
        Log.d(TAG, "Migration succeeded");
    }
}
