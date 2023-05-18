package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.measurement.Measurement
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one value of a [Measurement]
 */
data class MeasurementValue(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val value: Double,
    val typeId: Long,
    val measurementId: Long,
) : DatabaseEntity