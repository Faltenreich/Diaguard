package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing the unit of a [MeasurementType]
 */
data class MeasurementUnit(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val name: String,
    val factor: Double,
    val typeId: Long,
) : DatabaseEntity {

    lateinit var type: MeasurementType

    val isSelected: Boolean
        get() = type.selectedUnitId == id
}