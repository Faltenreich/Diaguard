package com.faltenreich.diaguard.shared.data.database.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrateOrmLiteToRoom {

    private static final String TAG = MigrateOrmLiteToRoom.class.getSimpleName();

    public MigrateOrmLiteToRoom() {

    }

    public void migrate(SupportSQLiteDatabase roomDatabase, SQLiteDatabase ormLiteDatabase) {
        Log.d(TAG, "Migrating database from OrmLite to Room");
        Cursor cursor = ormLiteDatabase.rawQuery("SELECT * FROM Tag", null);
        while (cursor.moveToNext()) {
            String value = cursor.getString(0);
        }
        cursor.close();
        // TODO: Maybe delete ormLiteDatabase
        Log.d(TAG, "Migration succeeded");
    }
}
