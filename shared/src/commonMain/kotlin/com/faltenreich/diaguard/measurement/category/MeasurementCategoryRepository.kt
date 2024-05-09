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

    fun getByKey(key: DatabaseKey.MeasurementCategory): MeasurementCategory {
        return checkNotNull(dao.getByKey(key.key))
    }

    fun getBloodSugar(): MeasurementCategory {
        return getByKey(DatabaseKey.MeasurementCategory.BLOOD_SUGAR)
    }

    fun getAll(): List<MeasurementCategory> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementCategory>> {
        return dao.observeAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ) {
        dao.update(
            id = id,
            updatedAt = updatedAt,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}