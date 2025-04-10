package com.faltenreich.diaguard.measurement.unit.suggestion

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface MeasurementUnitSuggestionDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        factor: Double,
        propertyId: Long,
        unitId: Long,
    )

    fun getLastId(): Long?

    fun observeByProperty(propertyId: Long): Flow<List<MeasurementUnitSuggestion.Local>>
}