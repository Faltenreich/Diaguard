package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
) {

    fun create(
        name: String,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = DateTime.now(),
            name = name,
            sortIndex = 0, // TODO: Current max + 1
            propertyId = propertyId,
        )
        return dao.getLastId() ?: throw IllegalStateException("No entry found")
    }

    fun getByPropertyId(propertyId: Long): Flow<List<MeasurementType>> {
        return dao.getByPropertyId(propertyId)
    }

    fun getByPropertyIds(propertyIds: List<Long>): Flow<List<MeasurementType>> {
        TODO()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long?,
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