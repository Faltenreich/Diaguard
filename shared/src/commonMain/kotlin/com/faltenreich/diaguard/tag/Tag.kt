package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.datetime.DateTime

/**
 * Entity for labeling an [Entry]
 */
data class Tag(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
) : DatabaseEntity