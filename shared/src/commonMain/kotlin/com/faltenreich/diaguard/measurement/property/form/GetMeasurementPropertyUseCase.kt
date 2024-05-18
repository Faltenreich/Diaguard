package com.faltenreich.diaguard.measurement.property.form

import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMeasurementPropertyUseCase(
    private val measurementCategoryRepository: MeasurementCategoryRepository = inject(),
    private val measurementUnitRepository: MeasurementUnitRepository = inject(),
) {

    operator fun invoke(property: MeasurementProperty): Flow<MeasurementProperty> {
        return measurementUnitRepository.observeByPropertyId(property.id).map { units ->
            property.apply {
                this.category = checkNotNull(measurementCategoryRepository.getById(categoryId))
                this.units = units.onEach { unit ->
                    unit.property = property
                }
            }
        }
    }
}