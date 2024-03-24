package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.type.deep
import com.faltenreich.diaguard.datetime.DateTime
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

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

    fun observeByDateRange(
        startDateTime: DateTime,
        endDateTime: DateTime,
    ): Flow<List<MeasurementValue>> {
        return dao.observeByDateRange(startDateTime, endDateTime)
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

    fun getAverageByTypeId(
        typeId: Long,
        minDateTime: DateTime,
        maxDateTime: DateTime
    ): Double? {
        return dao.getAverageByTypeId(typeId, minDateTime, maxDateTime)
    }

    fun observeCountByTypeId(typeId: Long): Flow<Long> {
        return dao.observeCountByTypeId(typeId)
    }

    fun countByPropertyId(propertyId: Long): Long {
        return dao.countByPropertyId(propertyId)
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

fun Flow<List<MeasurementValue>>.deep(
    entry: Entry,
    typeRepository: MeasurementTypeRepository = inject(),
): Flow<List<MeasurementValue>> {
    return flatMapLatest { values ->
        val flows = values.map { value ->
            typeRepository.observeById(value.typeId)
                .filterNotNull()
                .deep()
                .map { type -> value to type }
        }
        combine(flows) { valuesWithTypes ->
            valuesWithTypes.map { (value, type) ->
                value.entry = entry
                value.type = type
                value
            }
        }
    }
}

fun List<MeasurementValue>.deep(
    entry: Entry,
    typeRepository: MeasurementTypeRepository = inject(),
): List<MeasurementValue> {
    return map { value ->
        value.apply {
            val type = checkNotNull(typeRepository.getById(value.typeId))
            this.type = type.deep()
            this.entry = entry
        }
    }
}