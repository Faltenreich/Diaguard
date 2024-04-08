package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import kotlinx.coroutines.flow.Flow

interface MeasurementTypeDao {

    fun create(
        createdAt: DateTime,
        key: String?,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        selectedUnitId: Long,
        categoryId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementType?

    fun observeById(id: Long): Flow<MeasurementType?>

    fun getByKey(key: String): MeasurementType?

    fun getByCategoryId(categoryId: Long): List<MeasurementType>

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementType>>

    fun getAll(): List<MeasurementType>

    fun observeAll(): Flow<List<MeasurementType>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        range: MeasurementValueRange,
        selectedUnitId: Long,
    )

    fun deleteById(id: Long)
}