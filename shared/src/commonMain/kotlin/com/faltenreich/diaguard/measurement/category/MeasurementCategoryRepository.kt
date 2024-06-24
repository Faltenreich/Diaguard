package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class MeasurementCategoryRepository(
    private val dao: MeasurementCategoryDao = inject(),
    private val dateTimeFactory: DateTimeFactory = inject(),
) {

    fun create(category: MeasurementCategory.User): Long = with(category) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = null,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(category: MeasurementCategory.Seed): Long = with(category) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementCategory.Local? {
        return dao.getById(id)
    }

    fun observeByKey(key: DatabaseKey.MeasurementCategory): Flow<MeasurementCategory.Local?> {
        return dao.observeByKey(key.key)
    }

    fun observeBloodSugar(): Flow<MeasurementCategory.Local?> {
        return observeByKey(DatabaseKey.MeasurementCategory.BLOOD_SUGAR)
    }

    fun observeActive(): Flow<List<MeasurementCategory.Local>> {
        return dao.observeActive()
    }

    fun observeAll(): Flow<List<MeasurementCategory.Local>> {
        return dao.observeAll()
    }

    fun countAll(): Flow<Long> {
        return dao.countAll()
    }

    fun update(category: MeasurementCategory.Local) = with(category) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            icon = icon,
            sortIndex = sortIndex,
            isActive = isActive,
        )
    }

    fun delete(category: MeasurementCategory.Local) {
        dao.deleteById(category.id)
    }
}