package com.faltenreich.diaguard.shared.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

actual class DatabaseLegacyImport {

    actual fun import() {
        val context = inject<Context>()
        val databaseFile = context.getDatabasePath("diaguard.db")
        val database = SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)

        val entries = mutableListOf<Entry>()
        with (database.query("entry")) {
            while (moveToNext()) {
                val entry = Entry(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    dateTime = getDateTime("date"),
                    note = getString("note"),
                )
                entries.add(entry)
            }
            close()
        }

        val values = mutableListOf<MeasurementValue>()
        with (database.query("bloodsugar")) {
            while (moveToNext()) {
                val value = MeasurementValue(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("mgDl"),
                    typeId = 1, // TODO: Determine typeId of mgDl
                    entryId = getLong("entry"),
                )
                values.add(value)
            }
            close()
        }
        with (database.query("activity")) {
            while (moveToNext()) {
                val value = MeasurementValue(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("minutes"),
                    typeId = 1, // TODO: Determine typeId of minutes
                    entryId = getLong("entry"),
                )
                values.add(value)
            }
            close()
        }
        // TODO: Add other categories to values

        val tags = mutableListOf<String>()
        with (database.query("tag")) {
            while (moveToNext()) {
                try {
                    val id = getLong("_id")
                    val createdAt = getDateTime("createdAt")
                    val updatedAt = getDateTime("updatedAt")
                    val name = getString("name")
                    tags.add(name)
                } catch (exception: IllegalArgumentException) {
                    println(exception.message)
                }
            }
            close()
        }
        println("Found ${entries.size} entries, ${values.size} values and ${tags.size} tags")
    }
}

private fun SQLiteDatabase.query(table: String): Cursor {
    return query(table, null, null, null, null, null, null)
}

private fun Cursor.getLong(column: String): Long {
    return getLong(getColumnIndexOrThrow(column))
}

private fun Cursor.getDateTime(column: String): DateTime {
    return DateTime(millis = getLong(column))
}

private fun Cursor.getDouble(column: String): Double {
    return getDouble(getColumnIndexOrThrow(column))
}

private fun Cursor.getString(column: String): String {
    return getString(getColumnIndexOrThrow(column))
}