package com.faltenreich.diaguard.measurement.category

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementCategoryRepository(
    private val dao: MeasurementCategoryDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: DatabaseKey.MeasurementCategory?,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ): Long {
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

    fun update(
        id: Long,
        name: String,
        icon: String?,
        sortIndex: Long,
        isActive: Boolean,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
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