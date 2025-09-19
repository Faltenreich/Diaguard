package com.faltenreich.diaguard.measurement.unit.suggestion

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import kotlinx.coroutines.flow.Flow

class MeasurementUnitSuggestionRepository(
    private val dao: MeasurementUnitSuggestionDao,
    private val dateTimeFactory: DateTimeFactory,
) {

    fun create(
        unitSuggestion: MeasurementUnitSuggestion.Seed,
        propertyId: Long,
        unitId: Long,
    ): Long = with(unitSuggestion) {
        val now = dateTimeFactory.now()
        dao.create(
            createdAt = now,
            updatedAt = now,
            factor = factor,
            propertyId = propertyId,
            unitId = unitId,
        )
        return checkNotNull(dao.getLastId())
    }

    fun observeByProperty(propertyId: Long): Flow<List<MeasurementUnitSuggestion.Local>> {
        return dao.observeByProperty(propertyId)
    }
}