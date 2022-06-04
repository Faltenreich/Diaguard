package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.database.Cursor;
import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.RoomDatabase;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class MigrateTagsToRoom implements MigrateTableToRoom {

   private static final String TAG = MigrateTagsToRoom.class.getSimpleName();

   @Override
   public String getQuery() {
      return "SELECT * FROM Tag";
   }

   @Override
   public void migrate(Cursor cursor) {
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
            Log.e(TAG, "Failed to import Tag: " + exception);
         }
      }
      RoomDatabase database = Database.getInstance().getDatabase();
      TagDao tagDao = database.tagDao();
      // TODO: Remove check
      if (tagDao.getAll().isEmpty()) {
         for (Tag tag : tags) {
            tagDao.create(tag);
         }
      }
   }
}
