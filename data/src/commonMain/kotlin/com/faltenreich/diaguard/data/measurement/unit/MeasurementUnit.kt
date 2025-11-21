package com.faltenreich.diaguard.data.measurement.unit

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.DatabaseEntity
import com.faltenreich.diaguard.data.DatabaseKey

/**
 * Entity representing the unit of a [MeasurementProperty]
 */
sealed interface MeasurementUnit {

    val name: String
    val abbreviation: String

    data class Seed(
        override val name: String,
        override val abbreviation: String,
        val key: DatabaseKey.MeasurementUnit?,
    ) : MeasurementUnit

    data class User(
        override val name: String,
        override val abbreviation: String,
    ) : MeasurementUnit

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
        override val abbreviation: String,
        val key: DatabaseKey.MeasurementUnit?,
    ) : MeasurementUnit, DatabaseEntity
}