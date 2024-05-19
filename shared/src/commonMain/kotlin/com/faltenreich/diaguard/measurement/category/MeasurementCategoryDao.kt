package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

interface MeasurementCategoryDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    )

    fun getLastId(): Long?

    fun getById(id: Long): MeasurementCategory.Local?

    fun observeById(id: Long): Flow<MeasurementCategory.Local?>

    fun observeByKey(key: String): Flow<MeasurementCategory.Local?>

    fun observeActive(): Flow<List<MeasurementCategory.Local>>

    fun observeAll(): Flow<List<MeasurementCategory.Local>>

    fun countAll(): Flow<Long>

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    )

    fun deleteById(id: Long)
}