package com.faltenreich.diaguard.shared.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject

actual class DatabaseLegacyImport {

    actual fun import() {
        val context = inject<Context>()
        val databaseFile = context.getDatabasePath("diaguard.db")
        val database = SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)
        val entries = mutableListOf<Entry>()
        with (database.query("entry", null, null, null, null, null, null)) {
            while (moveToNext()) {
                val entry = Entry(
                    id = getLong(getColumnIndexOrThrow("_id")),
                    createdAt = DateTime(millis = getLong(getColumnIndexOrThrow("createdAt"))),
                    updatedAt = DateTime(millis = getLong(getColumnIndexOrThrow("updatedAt"))),
                    dateTime = DateTime(millis = getLong(getColumnIndexOrThrow("date"))),
                    note = getString(getColumnIndexOrThrow("note")),
                )
                entries.add(entry)
            }
            close()
        }
        val tags = mutableListOf<String>()
        with (database.query("tag", null, null, null, null, null, null)) {
            while (moveToNext()) {
                val tag = getString(getColumnIndexOrThrow("name"))
                tags.add(tag)
            }
            close()
        }
        println("Found ${entries.size} entries and ${tags.size} tags")
    }
}