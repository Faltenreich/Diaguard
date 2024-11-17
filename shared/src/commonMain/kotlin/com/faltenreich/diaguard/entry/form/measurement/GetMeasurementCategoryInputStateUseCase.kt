package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class GetMeasurementCategoryInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getCategories: GetActiveMeasurementCategoriesUseCase,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val getPreference: GetPreferenceUseCase,
    private val mapValue: MeasurementValueMapper,
) {

    suspend operator fun invoke(
        entry: Entry.Local?,
    ): Flow<List<MeasurementCategoryInputState>> = withContext(dispatcher) {
        combine(
            getCategories(),
            getPreference(DecimalPlacesPreference),
        ) { categories, decimalPlaces ->
            categories.mapIndexed { categoryIndex, category ->
                val properties = propertyRepository.getByCategoryId(category.id)
                val values = entry?.id?.let(valueRepository::getByEntryId)
                MeasurementCategoryInputState(
                    category = category,
                    propertyInputStates = properties.mapIndexed { propertyIndex, property ->
                        val value = values?.firstOrNull { it.property.id == property.id }
                        val isLast = categoryIndex == categories.size - 1 && propertyIndex == properties.size - 1
                        val input = value?.let { mapValue(value, decimalPlaces).value } ?: ""
                        MeasurementPropertyInputState(
                            property = property,
                            input = input,
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