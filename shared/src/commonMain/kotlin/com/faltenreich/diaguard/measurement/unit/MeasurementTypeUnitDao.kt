package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementTypeUnitDao {

    fun create(
        createdAt: DateTime,
        factor: Double,
        typeId: Long,
        unitId: Long,
    )

    fun getLastId(): Long?

    fun getByTypeId(typeId: Long): Flow<List<MeasurementTypeUnit>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        factor: Double,
    )

    fun deleteById(id: Long)
}