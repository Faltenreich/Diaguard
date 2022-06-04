package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.database.Cursor;
import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.Database;
import com.faltenreich.diaguard.shared.data.database.RoomDatabase;
import com.faltenreich.diaguard.shared.data.database.dao.EntryTagDao;
import com.faltenreich.diaguard.shared.data.database.dao.TagDao;
import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.database.entity.Tag;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class MigrateEntryTagsToRoom implements MigrateTableToRoom {

   private static final String TAG = MigrateEntryTagsToRoom.class.getSimpleName();

   @Override
   public String getQuery() {
      return "SELECT * FROM EntryTag";
   }

   @Override
   public void migrate(Cursor cursor) {
      List<EntryTag> entryTags = new ArrayList<>();
      while (cursor.moveToNext()) {
         try {
            EntryTag entryTag = new EntryTag();
            entryTag.setId(cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.ID)));
            entryTag.setCreatedAt(new DateTime((cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.CREATED_AT)))));
            entryTag.setUpdatedAt(new DateTime((cursor.getLong(getColumnIndex(cursor, BaseEntity.Column.UPDATED_AT)))));
            entryTag.setEntryId(cursor.getLong(getColumnIndex(cursor, EntryTag.Column.ENTRY)));
            entryTag.setTagId(cursor.getLong(getColumnIndex(cursor, EntryTag.Column.TAG)));
            entryTags.add(entryTag);
         } catch (IndexOutOfBoundsException exception) {
            Log.e(TAG, "Failed to import EntryTag: " + exception);
         }
      }
      RoomDatabase database = Database.getInstance().getDatabase();
      EntryTagDao entryTagDao = database.entryTagDao();
      // TODO: Remove check
      if (entryTagDao.count() == 0) {
         for (EntryTag entryTag : entryTags) {
            entryTagDao.create(entryTag);
         }
      }
   }
}
