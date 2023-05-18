package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitDao {

    fun create(
        createdAt: DateTime,
        name: String,
        factor: Double,
        property: MeasurementProperty,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Flow<MeasurementUnit?>

    fun getByProperty(property: MeasurementProperty): Flow<List<MeasurementUnit>>

    fun getAll(): Flow<List<MeasurementUnit>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        factor: Double,
    )

    fun deleteById(id: Long)
}