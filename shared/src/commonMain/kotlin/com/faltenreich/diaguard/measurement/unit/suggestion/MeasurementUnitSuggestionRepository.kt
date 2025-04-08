package com.faltenreich.diaguard.measurement.unit.suggestion

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory

class MeasurementUnitSuggestionRepository(
    private val dao: MeasurementUnitSuggestionDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(propertyUnit: MeasurementUnitSuggestion.Seed): Long = with(propertyUnit) {
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

    fun getById(id: Long): MeasurementUnitSuggestion.Local? {
        return dao.getById(id)
    }
}