package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity

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

    val isNotHighlighted: Boolean
        get() = type.range.isHighlighted.not()

    val isTooLow: Boolean
        get() = type.range.low?.let { value < it } ?: false

    val isTooHigh: Boolean
        get() = type.range.high?.let { value > it } ?: false
}