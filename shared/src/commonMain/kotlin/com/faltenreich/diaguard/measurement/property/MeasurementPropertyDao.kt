package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.measurement.value.range.MeasurementValueRange
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

interface MeasurementPropertyDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementProperty?,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        categoryId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementProperty.Local?

    fun observeById(id: Long): Flow<MeasurementProperty.Local?>

    fun observeByKey(key: String): Flow<MeasurementProperty.Local?>

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local>

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>>

    fun getAll(): List<MeasurementProperty.Local>

    fun observeAll(): Flow<List<MeasurementProperty.Local>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
    )

    fun deleteById(id: Long)
}