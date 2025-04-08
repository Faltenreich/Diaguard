package com.faltenreich.diaguard.measurement.property

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class MeasurementPropertyUnitRepository(
    private val dao: MeasurementPropertyUnitDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(propertyUnit: MeasurementPropertyUnit.Seed): Long = with(propertyUnit) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            factor = factor,
            propertyId = property.id,
            unitId = unit.id,
        )
        return checkNotNull(dao.getLastId())
    }

    fun getById(id: Long): MeasurementPropertyUnit.Local? {
        return dao.getById(id)
    }
}