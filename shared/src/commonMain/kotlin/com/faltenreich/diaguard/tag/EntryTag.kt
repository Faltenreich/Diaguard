package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.DatabaseEntity

/**
 * Entity linking a [Tag] to an [Entry]
 */
sealed interface EntryTag {

    val entry: Entry
    val tag: Tag

    data class Intermediate(
        override val entry: Entry,
        override val tag: Tag.Local,
    ) : EntryTag

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val entry: Entry,
        override val tag: Tag.Local,
    ) : EntryTag, DatabaseEntity
}