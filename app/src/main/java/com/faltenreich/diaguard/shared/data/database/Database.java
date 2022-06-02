package com.faltenreich.diaguard.shared.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.faltenreich.diaguard.shared.data.database.migration.MigrateOrmLiteToRoom;
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
            .addCallback(new androidx.room.RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase roomDatabase) {
                    super.onCreate(roomDatabase);
                    new MigrateOrmLiteToRoom().migrate(roomDatabase, databaseHelper.getReadableDatabase());
                }
                // TODO: Remove workaround which forces MigrateOrmLiteToRoom() to be called on every app start
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase roomDatabase) {
                    super.onOpen(roomDatabase);
                    new MigrateOrmLiteToRoom().migrate(roomDatabase, databaseHelper.getReadableDatabase());
                }
            })
            .build();
        // TODO: Remove workaround which forces RoomDatabase.onOpen() to be called
        roomDatabase.tagDao().getAll();
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
