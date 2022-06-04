package com.faltenreich.diaguard.shared.data.database.migration.ormlite;

import android.database.Cursor;

interface ColumnIndexSelector {

   default int getColumnIndex(Cursor cursor, String column) throws IndexOutOfBoundsException {
      int index = cursor.getColumnIndex(column);
      if (index == -1) {
         throw new IndexOutOfBoundsException();
      }
      return index;
   }
}
