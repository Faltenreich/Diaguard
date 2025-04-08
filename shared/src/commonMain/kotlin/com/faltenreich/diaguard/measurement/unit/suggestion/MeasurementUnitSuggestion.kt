package com.faltenreich.diaguard.measurement.unit.suggestion

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing a suggested unit of a seeded [MeasurementProperty]
 */
sealed interface MeasurementUnitSuggestion {

    val factor: Double

    val isDefault: Boolean
        get() = factor == FACTOR_DEFAULT

    data class Seed(
        override val factor: Double,
        val unit: DatabaseKey.MeasurementUnit,
    ) : MeasurementUnitSuggestion

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val factor: Double,
        val property: MeasurementProperty.Local,
        val unit: MeasurementUnit.Local,
    ) : MeasurementUnitSuggestion, DatabaseEntity

    companion object {

        const val FACTOR_DEFAULT = 1.0
    }
}