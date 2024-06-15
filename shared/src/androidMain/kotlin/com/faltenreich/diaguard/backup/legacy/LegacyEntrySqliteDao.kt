package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getLong
import com.faltenreich.diaguard.shared.database.sqlite.getString

class LegacyEntrySqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getEntries(): List<Entry.Legacy> {
        val entries = mutableListOf<Entry.Legacy>()
        database.query("entry") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val dateTime = getLong("date")?.let(dateTimeFactory::dateTime) ?: return@query
            entries.add(
                Entry.Legacy(
                    id = id,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    dateTime = dateTime,
                    note = getString("note"),
                )
            )
        }
        return entries
    }
}