package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        name: String,
        factor: Double,
        sortIndex: Long,
        typeId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Flow<MeasurementUnit?>

    fun getByTypeId(typeId: Long): Flow<List<MeasurementUnit>>

    fun observeAll(): Flow<List<MeasurementUnit>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        factor: Double,
        sortIndex: Long,
    )

    fun deleteById(id: Long)
}