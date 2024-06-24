package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
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

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun observeLatestByCategoryId(
        dateTime: DateTime,
        categoryId: Long,
    ): Flow<MeasurementValue.Local?> {
        return dao.observeLatestByCategoryId(
            dateTime = dateTime,
            categoryId = categoryId,
        )
    }

    fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return dao.getByEntryId(entryId)
    }

    fun observeCountByCategoryId(categoryId: Long): Flow<Long> {
        return dao.observeCountByCategoryId(categoryId)
    }

    fun observeByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue.Local>> {
        return dao.observeByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun observeAverageByCategoryId(
        categoryId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        return dao.observeAverageByCategoryId(categoryId, minDateTime, maxDateTime)
    }

    fun getAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        return dao.getAverageByPropertyId(propertyId, minDateTime, maxDateTime)
    }

    fun observeCountByPropertyId(propertyId: Long): Flow<Long> {
        return dao.observeCountByPropertyId(propertyId)
    }

    fun countByCategoryId(categoryId: Long): Long {
        return dao.countByCategoryId(categoryId)
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