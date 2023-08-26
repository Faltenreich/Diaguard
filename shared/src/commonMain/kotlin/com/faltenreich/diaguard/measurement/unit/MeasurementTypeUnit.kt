package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.DatabaseEntity
import com.faltenreich.diaguard.shared.datetime.DateTime

/**
 * Entity representing the m:n relation between [MeasurementType] and [MeasurementUnit]
 */
data class MeasurementTypeUnit(
    override val id: Long,
    override val createdAt: DateTime,
    override val updatedAt: DateTime,
    val factor: Double,
    val typeId: Long,
    val unitId: Long,
) : DatabaseEntity {

    lateinit var type: MeasurementType
    lateinit var unit: MeasurementUnit
}