package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfCategoryUseCase(
    private val measurementValueRepository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory): Flow<Long> {
        return measurementValueRepository.observeByCategoryId(category.id)
    }
}