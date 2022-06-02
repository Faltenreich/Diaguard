package com.faltenreich.diaguard.shared.data.database.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MigrateOrmLiteToRoom {

    private static final String TAG = MigrateOrmLiteToRoom.class.getSimpleName();

    public MigrateOrmLiteToRoom() {

    }

    public void migrate(SupportSQLiteDatabase roomDatabase, SQLiteDatabase ormLiteDatabase) {
        Log.d(TAG, "Migrating database from OrmLite to Room");
        Cursor cursor = ormLiteDatabase.rawQuery("SELECT * FROM Tag", null);
        List<Tag> tags = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                Tag tag = new Tag();
                tag.setId(cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.ID)));
                tag.setCreatedAt(new DateTime((cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.CREATED_AT)))));
                tag.setUpdatedAt(new DateTime((cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.UPDATED_AT)))));
                tag.setName(cursor.getString(getColumnIndex(cursor, Tag.Column.NAME)));
                tags.add(tag);
            } catch (IndexOutOfBoundsException exception) {
                Log.e(TAG, "Failed to import Tag: " + cursor);
            }
        }
        // TODO: Insert tags
        cursor.close();
        // TODO: Maybe delete ormLiteDatabase
        Log.d(TAG, "Migration succeeded");
    }

    private int getColumnIndex(Cursor cursor, String column) throws IndexOutOfBoundsException {
        int index = cursor.getColumnIndex(column);
        if (index == -1) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }
}
