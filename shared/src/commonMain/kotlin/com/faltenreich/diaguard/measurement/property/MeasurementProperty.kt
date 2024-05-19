package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.backup.seed.Seedable
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.database.DatabaseKey

/**
 * Entity representing one property of [MeasurementCategory]
 */
sealed interface MeasurementProperty {

    val name: String
    val sortIndex: Long
    val aggregationStyle: MeasurementAggregationStyle
    val range: MeasurementValueRange
    val category: MeasurementCategory

    data class User(
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        override val category: MeasurementCategory,
    ) : MeasurementProperty

    data class Seed(
        override val key: DatabaseKey.MeasurementProperty?,
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        override val category: MeasurementCategory,
    ) : MeasurementProperty, Seedable

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val key: DatabaseKey.MeasurementProperty?,
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        override val category: MeasurementCategory,
        val selectedUnit: MeasurementUnit.Local,
    ) : MeasurementProperty, DatabaseEntity, Seedable
}