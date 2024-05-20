package com.faltenreich.diaguard.measurement.unit

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementUnitRepository(
    private val dao: MeasurementUnitDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(unit: MeasurementUnit.User): Long = with(unit) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = null,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            isSelected = isSelected,
            propertyId = property.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun create(unit: MeasurementUnit.Seed): Long = with(unit) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            key = key,
            name = name,
            abbreviation = abbreviation,
            factor = factor,
            isSelected = isSelected,
            propertyId = propertyId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getByKey(key: String): MeasurementUnit.Local {
        return checkNotNull(dao.getByKey(key))
    }

    fun observeByPropertyId(propertyId: Long): Flow<List<MeasurementUnit.Local>> {
        return dao.observeByPropertyId(propertyId)
    }

    fun observeByCategoryId(categoryId: Long): Flow<List<MeasurementUnit.Local>> {
        return dao.observeByCategoryId(categoryId)
    }

    fun observeAll(): Flow<List<MeasurementUnit.Local>> {
        return dao.observeAll()
    }

    fun update(unit: MeasurementUnit.Local) = with(unit) {
        dao.update(
            id = id,
            updatedAt = dateTimeFactory.now(),
            name = name,
            abbreviation = abbreviation,
            isSelected = isSelected,
        )
    }

    fun delete(unit: MeasurementUnit.Local) {
        dao.deleteById(unit.id)
    }
}