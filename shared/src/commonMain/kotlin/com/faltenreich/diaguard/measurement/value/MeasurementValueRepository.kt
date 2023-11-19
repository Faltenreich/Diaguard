package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementValueRepository(
    private val dao: MeasurementValueDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        value: Double,
        typeId: Long,
        entryId: Long,
    ): Long {
        dao.create(
            createdAt = createdAt,
            updatedAt = updatedAt,
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(
        value: Double,
        typeId: Long,
        entryId: Long,
    ): Long {
        val now = dateTimeFactory.now()
        return create(
            createdAt = now,
            updatedAt = now,
            value = value,
            typeId = typeId,
            entryId = entryId,
        )
    }

    fun observeByEntryId(entryId: Long): Flow<List<MeasurementValue>> {
        return dao.observeByEntryId(entryId)
    }

    fun observeLatestByPropertyId(propertyId: Long): Flow<MeasurementValue?> {
        return dao.observeLatestByPropertyId(propertyId)
    }

    fun getByEntryId(entryId: Long): List<MeasurementValue> {
        return dao.getByEntryId(entryId)
    }

    fun observeByPropertyId(propertyId: Long): Flow<Long> {
        return dao.observeByPropertyId(propertyId)
    }

    fun observeByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<List<MeasurementValue>> {
        return dao.observeByPropertyId(propertyId, minDateTime, maxDateTime)
    }

    fun observeAverageByPropertyId(
        propertyId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Flow<Double?> {
        return dao.observeAverageByPropertyId(propertyId, minDateTime, maxDateTime)
    }

    fun observeCountByTypeId(typeId: Long): Flow<Long> {
        return dao.observeCountByTypeId(typeId)
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

    fun update(value: MeasurementValue) {
        update(
            id = value.id,
            value = value.value,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}