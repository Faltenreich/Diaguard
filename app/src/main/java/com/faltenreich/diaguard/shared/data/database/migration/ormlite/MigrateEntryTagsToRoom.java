package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.database.Cursor;
import android.util.Log;

import com.faltenreich.diaguard.shared.data.database.entity.BaseEntity;
import com.faltenreich.diaguard.shared.data.database.entity.EntryTag;
import com.faltenreich.diaguard.shared.data.repository.EntryTagRepository;

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
      EntryTagRepository.getInstance().createOrUpdate(entryTags);
      Log.d(TAG, "Migrated " + entryTags.size() + " entry tags");
   }
}
