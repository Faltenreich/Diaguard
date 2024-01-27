package com.faltenreich.diaguard.entry

import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.tag.EntryTag

/**
 * Entity representing one entry at a given point in time
 */
data class Entry(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val dateTime: DateTime,
    val note: String?,
) : DatabaseEntity {

    lateinit var values: List<MeasurementValue>
    lateinit var entryTags: List<EntryTag>
}