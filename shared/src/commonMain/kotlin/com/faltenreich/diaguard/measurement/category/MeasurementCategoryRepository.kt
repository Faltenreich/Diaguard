package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementCategoryRepository(
    private val dao: MeasurementCategoryDao,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        key: String?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            key = key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementCategory? {
        return dao.getById(id)
    }

    fun observeByKey(key: DatabaseKey.MeasurementCategory): Flow<MeasurementCategory?> {
        return dao.observeByKey(key.key)
    }

    fun observeBloodSugar(): Flow<MeasurementCategory?> {
        return observeByKey(DatabaseKey.MeasurementCategory.BLOOD_SUGAR)
    }

    fun observeActive(): Flow<List<MeasurementCategory>> {
        return dao.observeActive()
    }

    fun observeAll(): Flow<List<MeasurementCategory>> {
        return dao.observeAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(category: MeasurementCategory) {
        dao.update(category)
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}