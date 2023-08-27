package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        name: String,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Flow<MeasurementUnit?>

    fun observeAll(): Flow<List<MeasurementUnit>>

    fun update(unit: MeasurementUnit)

    fun deleteById(id: Long)
}