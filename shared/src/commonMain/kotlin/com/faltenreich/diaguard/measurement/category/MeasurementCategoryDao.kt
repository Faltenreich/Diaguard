package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementCategoryDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: String?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementCategory?

    fun observeById(id: Long): Flow<MeasurementCategory?>

    fun observeByKey(key: String): Flow<MeasurementCategory?>

    fun observeActive(): Flow<List<MeasurementCategory>>

    fun observeAll(): Flow<List<MeasurementCategory>>

    fun countAll(): Flow<Long>

    fun update(category: MeasurementCategory)

    fun deleteById(id: Long)
}