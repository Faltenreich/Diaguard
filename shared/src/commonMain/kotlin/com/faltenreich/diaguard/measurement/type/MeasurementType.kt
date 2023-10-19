package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.backup.seed.Seedable
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
    override val key: String?,
    val name: String,
    val sortIndex: Long,
    val selectedUnitId: Long?,
    val propertyId: Long,
) : DatabaseEntity, Seedable {

    lateinit var property: MeasurementProperty
    lateinit var units: List<MeasurementUnit>

    var selectedUnit: MeasurementUnit? = null
}