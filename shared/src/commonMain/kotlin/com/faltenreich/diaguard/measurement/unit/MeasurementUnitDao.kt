package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        name: String,
        factor: Double,
        typeId: Long,
    )

    fun getLastId(): Long?

    fun observeById(id: Long): Flow<MeasurementUnit?>

    fun observeByTypeId(typeId: Long): Flow<List<MeasurementUnit>>

    fun observeAll(): Flow<List<MeasurementUnit>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    )

    fun deleteById(id: Long)
}