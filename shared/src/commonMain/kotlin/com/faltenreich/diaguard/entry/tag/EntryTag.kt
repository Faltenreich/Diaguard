package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.tag.Tag

/**
 * Entity linking a [Tag] to an [Entry]
 */
sealed interface EntryTag {

    val entry: Entry
    val tag: Tag

    data class Legacy(
        val createdAt: DateTime,
        val updatedAt: DateTime,
        val entryId: Long,
        val tagId: Long,
    ) : EntryTag {

        override lateinit var entry: Entry.Local
        override lateinit var tag: Tag.Local
    }

    data class Intermediate(
        override val entry: Entry.Local,
        override val tag: Tag.Local,
    ) : EntryTag

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val entry: Entry.Local,
        override val tag: Tag.Local,
    ) : EntryTag, DatabaseEntity

    data class Localized(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val entry: Entry.Localized,
        override val tag: Tag.Localized,
    ) : EntryTag, DatabaseEntity
}