package com.faltenreich.diaguard.measurement.property

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

    data class Seed(
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        val key: DatabaseKey.MeasurementProperty?,
        val units: List<MeasurementUnit.Seed>,
    ) : MeasurementProperty

    data class User(
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        val category: MeasurementCategory.Local,
    ) : MeasurementProperty

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        val key: DatabaseKey.MeasurementProperty?,
        val category: MeasurementCategory.Local,
    ) : MeasurementProperty, DatabaseEntity {

        lateinit var selectedUnit: MeasurementUnit.Local

        val isUserGenerated: Boolean
            get() = key == null
    }
}