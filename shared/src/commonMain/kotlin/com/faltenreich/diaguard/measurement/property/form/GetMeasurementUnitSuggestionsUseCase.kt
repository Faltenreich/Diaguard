package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementUnitSuggestionsUseCase(private val repository: MeasurementUnitSuggestionRepository) {

    operator fun invoke(propertyId: Long): Flow<List<MeasurementUnitSuggestion.Local>> {
        return repository.observeByProperty(propertyId)
    }
}