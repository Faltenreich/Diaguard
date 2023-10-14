package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        name: String,
        sortIndex: Long,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            name = name,
            sortIndex = sortIndex,
            propertyId = propertyId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun observeById(id: Long): Flow<MeasurementType?> {
        return dao.observeById(id)
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementType>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun getById(id: Long): MeasurementType? {
        return dao.getById(id)
    }

    fun getByPropertyId(propertyId: Long): List<MeasurementType> {
        return dao.getByPropertyId(propertyId)
    }

    fun observeAll(): Flow<List<MeasurementType>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long?,
    ) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            sortIndex = sortIndex,
            selectedUnitId = selectedUnitId,
        )
    }

    fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}

fun Flow<MeasurementType>.deep(
    propertyRepository: MeasurementPropertyRepository = inject(),
): Flow<MeasurementType> {
    return flatMapLatest { type ->
        val flows = propertyRepository.observeById(type.propertyId)
            .filterNotNull()
            .map { property -> type to property }
        combine(flows) { typeWithProperty ->
            typeWithProperty.map { (type, property) ->
                // TODO: Set selected unit
                type.property = property
                type
            }.first()
        }
    }
}

fun MeasurementType.deep(
    propertyRepository: MeasurementPropertyRepository = inject(),
): MeasurementType {
    return apply {
        this.property = propertyRepository.getById(propertyId) ?: throw IllegalStateException()
        // TODO: Selected unit
    }
}