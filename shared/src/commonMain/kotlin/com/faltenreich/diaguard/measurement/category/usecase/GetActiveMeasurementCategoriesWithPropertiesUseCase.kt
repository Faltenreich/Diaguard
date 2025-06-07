package com.faltenreich.diaguard.measurement.category.usecase

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetActiveMeasurementCategoriesWithPropertiesUseCase(
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
) {

    operator fun invoke(): Flow<Map<MeasurementCategory.Local, List<MeasurementProperty.Local>>> {
        return categoryRepository.observeActive().flatMapLatest { categories ->
            combine(
                categories.map { category ->
                    propertyRepository.observeByCategoryId(category.id).map { properties ->
                        category to properties
                    }
                },
                Array<Pair<MeasurementCategory.Local, List<MeasurementProperty.Local>>>::toMap,
            )
        }
    }
}