package com.faltenreich.diaguard.measurement.unit

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

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    data class Seed(
        override val name: String,
        override val abbreviation: String,
        override val factor: Double,
        override val isSelected: Boolean,
        val key: DatabaseKey.MeasurementUnit?,
    ) : MeasurementUnit

    data class User(
        override val name: String,
        override val abbreviation: String,
        override val factor: Double,
        override val isSelected: Boolean,
        val propertyId: Long,
    ) : MeasurementUnit

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
        override val abbreviation: String,
        override val factor: Double,
        override val isSelected: Boolean,
        val key: DatabaseKey.MeasurementUnit?,
        val property: MeasurementProperty.Local,
    ) : MeasurementUnit, DatabaseEntity

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }
}