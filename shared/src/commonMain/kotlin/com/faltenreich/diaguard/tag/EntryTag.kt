package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.datetime.DateTime

/**
 * Entity linking a [Tag] to an [Entry]
 */
data class EntryTag(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val entryId: Long,
    val tagId: Long,
) : DatabaseEntity {

    lateinit var entry: Entry
    lateinit var tag: Tag
}