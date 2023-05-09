package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing the unit of a [Measurement]
 */
data class MeasurementUnit(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
    val factor: Float,
    val property: MeasurementProperty,
) : DatabaseEntity