package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one reading of a [MeasurementProperty] at one moment in time
 *
 *
 */
data class Measurement(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val property: MeasurementProperty,
    val entry: Entry,
) : DatabaseEntity