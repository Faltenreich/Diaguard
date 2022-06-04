package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

interface MigrateTableToRoom extends ColumnIndexSelector {

   default void migrate(SQLiteDatabase ormLiteDatabase) {
      String query = getQuery();
      Cursor cursor = ormLiteDatabase.rawQuery(query, null);
      migrate(cursor);
      cursor.close();
   }

   String getQuery();

   void migrate(Cursor cursor);
}
