package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.shared.datetime.DateTime
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
) {

    fun create(
        name: String,
        selectedUnitId: Long,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            sortIndex = 0, // TODO: Current max + 1
            selectedUnitId = selectedUnitId,
            propertyId = propertyId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun observeById(id: Long): Flow<MeasurementType?> {
        return dao.observeById(id)
    }

    fun getById(id: Long): MeasurementType? {
        return dao.getById(id)
    }

    fun getByPropertyId(propertyId: Long): List<MeasurementType> {
        return dao.getByPropertyId(propertyId)
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
    ) {
        dao.update(
            id = id,
            updatedAt = DateTime.now(),
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
    return map { type ->
        type.apply {
            this.property = propertyRepository.observeById(propertyId).filterNotNull().first()
            // TODO: Selected unit
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