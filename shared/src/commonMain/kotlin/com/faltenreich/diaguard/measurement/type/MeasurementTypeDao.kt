package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementTypeDao {

    fun create(
        createdAt: DateTime,
        key: String?,
        name: String,
        minimumValue: Double,
        lowValue: Double?,
        highValue: Double?,
        maximumValue: Double,
        sortIndex: Long,
        selectedUnitId: Long,
        propertyId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementType?

    fun observeById(id: Long): Flow<MeasurementType?>

    fun getByKey(key: String): MeasurementType?

    fun getByPropertyId(propertyId: Long): List<MeasurementType>

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementType>>

    fun getAll(): List<MeasurementType>

    fun observeAll(): Flow<List<MeasurementType>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        minimumValue: Double,
        lowValue: Double?,
        highValue: Double?,
        maximumValue: Double,
        sortIndex: Long,
        selectedUnitId: Long,
    )

    fun deleteById(id: Long)
}