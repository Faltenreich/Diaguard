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

    fun observeAll(): Flow<List<MeasurementTypeUnit>>

    fun update(typeUnit: MeasurementTypeUnit)

    fun deleteById(id: Long)
}