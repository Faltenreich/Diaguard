package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyle
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRange
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
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
    val category: MeasurementCategory.Local?
    val unit: MeasurementUnit.Local?

    data class Seed(
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        val key: DatabaseKey.MeasurementProperty?,
        val unitSuggestions: List<MeasurementUnitSuggestion.Seed>,
    ) : MeasurementProperty {

        override lateinit var category: MeasurementCategory.Local
        override lateinit var unit: MeasurementUnit.Local
    }

    data class User(
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        override val category: MeasurementCategory.Local,
        override var unit: MeasurementUnit.Local?,
    ) : MeasurementProperty {

        constructor(
            sortIndex: Long,
            category: MeasurementCategory.Local,
        ) : this(
            name = "",
            sortIndex = sortIndex,
            aggregationStyle = MeasurementAggregationStyle.CUMULATIVE,
            range = MeasurementValueRange(
                minimum = 0.0,
                low = null,
                target = null,
                high = null,
                maximum = 10_000.0,
                isHighlighted = false,
            ),
            category = category,
            unit = null,
        )
    }

    data class Local(
        override val id: Long,
        override val createdAt: DateTime,
        override val updatedAt: DateTime,
        override val name: String,
        override val sortIndex: Long,
        override val aggregationStyle: MeasurementAggregationStyle,
        override val range: MeasurementValueRange,
        override val category: MeasurementCategory.Local,
        override val unit: MeasurementUnit.Local,
        val key: DatabaseKey.MeasurementProperty?,
        val valueFactor: Double,
    ) : MeasurementProperty, DatabaseEntity {

        val isUserGenerated: Boolean = key == null
    }
}