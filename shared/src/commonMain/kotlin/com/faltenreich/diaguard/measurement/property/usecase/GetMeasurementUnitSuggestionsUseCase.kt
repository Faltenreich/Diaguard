package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementUnitSuggestionsUseCase(private val repository: MeasurementUnitSuggestionRepository) {

    operator fun invoke(property: MeasurementProperty.Local): Flow<List<MeasurementUnitSuggestion.Local>> {
        return repository.observeByProperty(property.id)
    }
}