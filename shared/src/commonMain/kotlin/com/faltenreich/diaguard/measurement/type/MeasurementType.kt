package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing one type of [MeasurementProperty]
 *
 * TODO: Possible and target ranges
 */
data class MeasurementType(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
    val sortIndex: Long,
    val propertyId: Long,
) : DatabaseEntity {

    lateinit var property: MeasurementProperty
    lateinit var units: List<MeasurementUnit>

    val selectedUnit: MeasurementUnit?
        get() = units.minByOrNull(MeasurementUnit::sortIndex)
}