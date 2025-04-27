package com.faltenreich.diaguard.measurement.property.usecase

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementPropertiesUseCase(private val repository: MeasurementPropertyRepository) {

    operator fun invoke(category: MeasurementCategory.Local): Flow<List<MeasurementProperty.Local>> {
        return repository.observeByCategoryId(category.id)
    }
}