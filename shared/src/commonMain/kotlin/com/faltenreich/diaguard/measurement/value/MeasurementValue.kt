package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.datetime.DateTime

/**
 * Entity representing a default value of an [Entry]
 */
data class MeasurementValue(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val value: Double,
    val typeId: Long,
    val entryId: Long,
) : DatabaseEntity {

    lateinit var type: MeasurementType
    lateinit var entry: Entry
}