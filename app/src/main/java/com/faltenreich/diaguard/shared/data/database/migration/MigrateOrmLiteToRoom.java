package com.faltenreich.diaguard.shared.data.database.migration;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.faltenreich.diaguard.shared.data.database.DatabaseVersion;

public class MigrateOrmLiteToRoom extends Migration {

    private static final String TAG = MigrateOrmLiteToRoom.class.getSimpleName();

    public MigrateOrmLiteToRoom() {
        super(DatabaseVersion.v3_1, DatabaseVersion.v4_0);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        Log.d(TAG, "Migrating database from OrmLite to Room");
    }
}
