package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfCategoryUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(category: MeasurementCategory.Local): Flow<Long> {
        return repository.observeCountByCategoryId(category.id)
    }
}