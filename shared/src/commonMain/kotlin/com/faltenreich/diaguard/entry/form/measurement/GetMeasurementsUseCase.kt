package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository

class GetMeasurementsUseCase(
    private val measurementPropertyRepository: MeasurementPropertyRepository,
    private val measurementTypeRepository: MeasurementTypeRepository,
    private val measurementValueRepository: MeasurementValueRepository,
) {

    operator fun invoke(entry: Entry?): List<MeasurementPropertyInputData> {
        val entryId = entry?.id
        val values = entryId?.let(measurementValueRepository::getByEntryId)
        return measurementPropertyRepository.getAll().map { property ->
            val types = measurementTypeRepository.getByPropertyId(property.id)
            MeasurementPropertyInputData(
                property = property,
                typeInputDataList = types.map { type ->
                    val value = values?.firstOrNull { it.typeId == type.id }
                    MeasurementTypeInputData(
                        type = type,
                        input = value?.value?.toString() ?: "",
                    )
                }
            )
        }
    }
}