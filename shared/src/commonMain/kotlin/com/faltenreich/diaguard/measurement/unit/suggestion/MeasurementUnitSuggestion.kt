package com.faltenreich.diaguard.measurement.unit.suggestion

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity

sealed interface MeasurementUnitSuggestion {

    val factor: Double
    val property: MeasurementProperty.Local
    val unit: MeasurementUnit.Local

    data class Seed(
        override val factor: Double,
        override val property: MeasurementProperty.Local,
        override val unit: MeasurementUnit.Local,
    ) : MeasurementUnitSuggestion

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val factor: Double,
        override val property: MeasurementProperty.Local,
        override val unit: MeasurementUnit.Local,
    ) : MeasurementUnitSuggestion, DatabaseEntity
}