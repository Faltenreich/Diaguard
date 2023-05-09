package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one value of a [Measurement]
 */
data class MeasurementValue(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val value: Float,
    val measurement: Measurement,
) : DatabaseEntity