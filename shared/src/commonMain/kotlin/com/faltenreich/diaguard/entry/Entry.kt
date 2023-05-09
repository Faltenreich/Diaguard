package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one entry at a given point in time
 */
data class Entry(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val dateTime: DateTime,
    val note: String?,
) : DatabaseEntity