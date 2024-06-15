package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getLong
import com.faltenreich.diaguard.shared.database.sqlite.getString
import com.faltenreich.diaguard.tag.Tag

class LegacyTagSqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun getTags(): List<Tag.Legacy> {
        val tags = mutableListOf<Tag.Legacy>()
        database.query("tag") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val name = getString("name") ?: return@query
            tags.add(
                Tag.Legacy(
                    id = id,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    name = name,
                )
            )
        }
        return tags
    }
}