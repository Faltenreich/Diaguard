package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.DecimalPlaces
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetMeasurementCategoryInputStateUseCase(
    private val getCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
) {

    operator fun invoke(entry: Entry.Local?): Flow<List<MeasurementCategoryInputState>> {
        return combine(
            getCategories(),
            getPreference(DecimalPlaces),
        ) { categories, decimalPlaces ->
            categories.mapIndexed { categoryIndex, category ->
                // FIXME: Observe via Flow
                val properties = propertyRepository.getByCategoryId(category.id)
                val values = entry?.id?.let(valueRepository::getByEntryId)
                MeasurementCategoryInputState(
                    category = category,
                    propertyInputStates = properties.mapIndexed { propertyIndex, property ->
                        val value = values?.firstOrNull { it.property.id == property.id }
                        val isLast = categoryIndex == categories.size - 1 && propertyIndex == properties.size - 1
                        MeasurementPropertyInputState(
                            property = property,
                            input = value?.value?.toString() ?: "",
                            isLast = isLast,
                            error = null,
                            decimalPlaces = decimalPlaces,
                        )
                    },
                    error = null,
                )
            }
        }
    }
}