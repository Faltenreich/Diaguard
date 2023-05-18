package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one reading of a [MeasurementType] at one moment in time
 */
data class Measurement(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val typeId: Long,
    val entryId: Long,
) : DatabaseEntity