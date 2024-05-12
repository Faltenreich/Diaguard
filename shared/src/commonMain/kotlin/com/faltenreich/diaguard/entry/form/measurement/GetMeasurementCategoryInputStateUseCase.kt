package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.list.GetMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMeasurementCategoryInputStateUseCase(
    private val getCategories: GetMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
) {

    operator fun invoke(entry: Entry?): Flow<List<MeasurementCategoryInputState>> {
        return getCategories().map { categories ->
            categories.mapIndexed { categoryIndex, category ->
                // FIXME: Observe via Flow
                val properties = propertyRepository.getByCategoryId(category.id)
                val values = entry?.id?.let(valueRepository::getByEntryId)
                MeasurementCategoryInputState(
                    category = category,
                    propertyInputStates = properties.mapIndexed { propertyIndex, property ->
                        val value = values?.firstOrNull { it.propertyId == property.id }
                        val isLast = categoryIndex == categories.size - 1 && propertyIndex == properties.size - 1
                        MeasurementPropertyInputState(
                            property = property,
                            input = value?.value?.toString() ?: "",
                            isLast = isLast,
                            error = null,
                        )
                    },
                    error = null,
                )
            }
        }
    }
}