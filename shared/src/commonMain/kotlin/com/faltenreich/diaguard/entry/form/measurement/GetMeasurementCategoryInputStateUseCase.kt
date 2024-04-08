package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetMeasurementCategoryInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val measurementCategoryRepository: MeasurementCategoryRepository,
    private val measurementTypeRepository: MeasurementTypeRepository,
    private val measurementValueRepository: MeasurementValueRepository,
) {

    suspend operator fun invoke(entry: Entry?): List<MeasurementCategoryInputState> = withContext(dispatcher) {
        val entryId = entry?.id
        val values = entryId?.let(measurementValueRepository::getByEntryId)
        val categories = measurementCategoryRepository.getAll()
        categories.mapIndexed { categoryIndex, category ->
            val types = measurementTypeRepository.getByCategoryId(category.id)
            MeasurementCategoryInputState(
                category = category,
                typeInputStates = types.mapIndexed { typeIndex, type ->
                    val value = values?.firstOrNull { it.typeId == type.id }
                    val isLast = categoryIndex == categories.size - 1 && typeIndex == types.size - 1
                    MeasurementTypeInputState(
                        type = type,
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