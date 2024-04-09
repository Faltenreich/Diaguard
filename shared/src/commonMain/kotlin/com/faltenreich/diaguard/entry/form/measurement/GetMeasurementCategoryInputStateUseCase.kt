package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetMeasurementCategoryInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val measurementCategoryRepository: MeasurementCategoryRepository,
    private val measurementPropertyRepository: MeasurementPropertyRepository,
    private val measurementValueRepository: MeasurementValueRepository,
) {

    suspend operator fun invoke(entry: Entry?): List<MeasurementCategoryInputState> = withContext(dispatcher) {
        val entryId = entry?.id
        val values = entryId?.let(measurementValueRepository::getByEntryId)
        val categories = measurementCategoryRepository.getAll()
        categories.mapIndexed { categoryIndex, category ->
            val properties = measurementPropertyRepository.getByCategoryId(category.id)
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