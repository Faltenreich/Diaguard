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
data class MeasurementProperty(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    override val key: DatabaseKey.MeasurementProperty?,
    val name: String,
    val sortIndex: Long,
    val aggregationStyle: MeasurementAggregationStyle,
    val range: MeasurementValueRange,
    val categoryId: Long,
) : DatabaseEntity, Seedable {

    lateinit var category: MeasurementCategory
    lateinit var units: List<MeasurementUnit.Local>

    // TODO: Remove erroneous lateinit properties
    val selectedUnit: MeasurementUnit.Local
        get() = units.first(MeasurementUnit::isSelected)
}