package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing the unit of a [MeasurementProperty]
 */
sealed interface MeasurementUnit {

    val name: String
    val abbreviation: String
    val factor: Double
    val isSelected: Boolean
    val property: MeasurementProperty

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    data class User(
        override val name: String,
        override val abbreviation: String,
        override val factor: Double,
        override val isSelected: Boolean,
        override val property: MeasurementProperty,
    ) : MeasurementUnit

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val key: DatabaseKey.MeasurementUnit?,
        override val name: String,
        override val abbreviation: String,
        override val factor: Double,
        override val isSelected: Boolean,
        override val property: MeasurementProperty,
    ) : MeasurementUnit, DatabaseEntity, Seedable

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }
}