package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow

class CountMeasurementValuesOfCategoryUseCase(
    private val repository: MeasurementValueRepository = inject(),
) {

    operator fun invoke(category: MeasurementCategory.Local): Flow<Long> {
        return repository.observeCountByCategoryId(category.id)
    }
}