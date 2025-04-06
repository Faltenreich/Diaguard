package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity

sealed interface MeasurementPropertyUnit {

    val factor: Double
    val property: MeasurementProperty.Local
    val unit: MeasurementUnit.Local

    data class User(
        override val factor: Double,
        override val property: MeasurementProperty.Local,
        override val unit: MeasurementUnit.Local,
    ) : MeasurementPropertyUnit

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val factor: Double,
        override val property: MeasurementProperty.Local,
        override val unit: MeasurementUnit.Local,
    ) : MeasurementPropertyUnit, DatabaseEntity
}