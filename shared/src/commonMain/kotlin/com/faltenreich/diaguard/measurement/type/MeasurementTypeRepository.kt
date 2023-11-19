package com.faltenreich.diaguard.measurement.type

import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementTypeRepository(
    private val dao: MeasurementTypeDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        key: String?,
        name: String,
        sortIndex: Long,
        propertyId: Long,
    ): Long {
        dao.create(
            createdAt = dateTimeFactory.now(),
            key = key,
            name = name,
            sortIndex = sortIndex,
            // We set this temporary id because the corresponding unit will be created afterwards
            selectedUnitId = MeasurementType.SELECTED_UNIT_ID_INVALID,
            propertyId = propertyId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementType? {
        return dao.getById(id)
    }

    fun observeById(id: Long): Flow<MeasurementType?> {
        return dao.observeById(id)
    }

    fun getByKey(key: String): MeasurementType {
        return checkNotNull(dao.getByKey(key))
    }

    fun getByPropertyId(propertyId: Long): List<MeasurementType> {
        return dao.getByPropertyId(propertyId)
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementType>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun getAll(): List<MeasurementType> {
        return dao.getAll()
    }

    fun observeAll(): Flow<List<MeasurementType>> {
        return dao.observeAll()
    }

    fun update(
        id: Long,
        name: String,
        sortIndex: Long,
        selectedUnitId: Long,
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