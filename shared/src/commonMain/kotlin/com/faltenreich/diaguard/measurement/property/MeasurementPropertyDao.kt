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
        selectedUnitId: Long?,
        categoryId: Long,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementProperty?

    fun observeById(id: Long): Flow<MeasurementProperty?>

    fun getByKey(key: String): MeasurementProperty?

    fun getByCategoryId(categoryId: Long): List<MeasurementProperty>

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementProperty>>

    fun getAll(): List<MeasurementProperty>

    fun observeAll(): Flow<List<MeasurementProperty>>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        sortIndex: Long,
        aggregationStyle: MeasurementAggregationStyle,
        range: MeasurementValueRange,
        selectedUnitId: Long?,
    )

    fun deleteById(id: Long)
}