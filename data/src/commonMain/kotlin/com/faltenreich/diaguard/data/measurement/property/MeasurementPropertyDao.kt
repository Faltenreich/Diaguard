package com.faltenreich.diaguard.data.measurement.property

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.data.DatabaseKey
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
        unitId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementProperty.Local?

    fun observeById(id: Long): Flow<MeasurementProperty.Local?>

    fun observeByKey(key: DatabaseKey.MeasurementProperty): Flow<MeasurementProperty.Local?>

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty.Local>

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty.Local>>

    fun observeByCategoryKey(categoryKey: DatabaseKey.MeasurementCategory): Flow<List<MeasurementProperty.Local>>

    fun observeIfCategoryIsActive(
        excludedPropertyKey: DatabaseKey.MeasurementProperty,
    ): Flow<List<MeasurementProperty.Local>>

    fun getAll(): List<MeasurementProperty.Local>

    fun observeAll(): Flow<List<MeasurementProperty.Local>>

    fun getMaximumSortIndex(categoryId: Long): Long?

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        unitId: Long,
    )

    fun deleteById(id: Long)
}