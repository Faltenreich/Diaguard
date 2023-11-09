package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetMeasurementsInputDataUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val measurementPropertyRepository: MeasurementPropertyRepository,
    private val measurementTypeRepository: MeasurementTypeRepository,
    private val measurementValueRepository: MeasurementValueRepository,
) {

    suspend operator fun invoke(entry: Entry?): List<MeasurementPropertyInputData> = withContext(dispatcher) {
        val entryId = entry?.id
        val values = entryId?.let(measurementValueRepository::getByEntryId)
        val properties = measurementPropertyRepository.getAll()
        properties.mapIndexed { propertyIndex, property ->
            val types = measurementTypeRepository.getByPropertyId(property.id)
            MeasurementPropertyInputData(
                property = property,
                typeInputDataList = types.mapIndexed { typeIndex, type ->
                    val value = values?.firstOrNull { it.typeId == type.id }
                    val isLast = propertyIndex == properties.size - 1 && typeIndex == types.size - 1
                    MeasurementTypeInputData(
                        type = type,
                        input = value?.value?.toString() ?: "",
                        isLast = isLast,
                    )
                }
            )
        }
    }
}