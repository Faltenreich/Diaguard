package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.DatabaseEntity

/**
 * Entity for labeling an [Entry]
 */
sealed interface Tag {

    val name: String

    data class User(
        override val name: String,
    ) : Tag

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
    ) : Tag, DatabaseEntity
}