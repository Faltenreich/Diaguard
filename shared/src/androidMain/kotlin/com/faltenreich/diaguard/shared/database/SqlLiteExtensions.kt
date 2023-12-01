package com.faltenreich.diaguard.shared.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.logging.Logger

fun SQLiteDatabase.queryEach(table: String, onEach: Cursor.() -> Unit) {
    with (query(table, null, null, null, null, null, null)) {
        while (moveToNext()) {
            try {
                onEach()
            } catch (exception: IllegalArgumentException) {
                Logger.error("Failed to query table $table", exception)
            }
        }
        close()
    }
}

fun Cursor.getLong(column: String): Long {
    return getLong(getColumnIndexOrThrow(column))
}

fun Cursor.getDateTime(column: String): DateTime {
    return inject<DateTimeFactory>().dateTime(millis = getLong(column))
}

fun Cursor.getDouble(column: String): Double {
    return getDouble(getColumnIndexOrThrow(column))
}

fun Cursor.getString(column: String): String {
    return getString(getColumnIndexOrThrow(column))
}