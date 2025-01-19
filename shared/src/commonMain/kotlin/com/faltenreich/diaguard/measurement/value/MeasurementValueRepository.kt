package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.DatabaseKey
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(value: MeasurementValue.User): Long = with(value) {
        dao.create(
            createdAt = dateTimeFactory.now(),
            updatedAt = dateTimeFactory.now(),
            value = value.value,
            propertyId = property.id,
            entryId = entry.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(value: MeasurementValue.Legacy): Long = with(value) {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            value = value.value,
            propertyId = property.id,
            entryId = entry.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return dao.getByEntryId(entryId)
    }

    fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return dao.observeByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun observeByCategoryKeyAndDateRange(
        categoryKey: DatabaseKey.MeasurementCategory,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return dao.observeByCategoryKeyAndDateRange(categoryKey, minDateTime, maxDateTime)
    }

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue.Local>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun observeLatestByProperty(
        key: DatabaseKey.MeasurementProperty,
    ): Flow<MeasurementValue.Local?> {
        return dao.observeLatestByProperty(key = key)
    }

    fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return dao.observeCountByPropertyId(propertyId)
    }

    fun observeCountByCategoryId(categoryId: Long): Flow<Long> {
        return dao.observeCountByCategoryId(categoryId)
    }

    fun observeAverageByPropertyKey(
        propertyKey: DatabaseKey.MeasurementProperty,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?> {
        return dao.observeAverageByPropertyKey(propertyKey, minDateTime, maxDateTime)
    }

    fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<Double?> {
        return dao.observeAverageByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun observeAveragesByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime,
    ): Flow<List<MeasurementValue.Average>> {
        return dao.observeAveragesByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun update(value: MeasurementValue.Local) {
        dao.update(
            id = value.id,
            updatedAt = dateTimeFactory.now(),
            value = value.value,
        )
    }

    fun delete(value: MeasurementValue.Local) {
        dao.deleteById(value.id)
    }
}