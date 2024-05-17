package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        // Only set by legacy import
        createdAt: DateTime = dateTimeFactory.now(),
        updatedAt: DateTime = dateTimeFactory.now(),

        value: Double,
        propertyId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            value = value,
            propertyId = propertyId,
            entryId = entryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
    }

    fun observeLatestByCategoryId(categoryId: Long): Flow<MeasurementValue.Local?> {
        return dao.observeLatestByCategoryId(categoryId)
    }

    fun getByEntryId(entryId: Long): List<MeasurementValue.Local> {
        return dao.getByEntryId(entryId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<Long> {
        return dao.observeByCategoryId(categoryId)
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

    fun update(
        id: Long,
        value: Double,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            value = value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}