package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState

class CreateMeasurementValuesUseCase(
    private val measurementValueRepository: MeasurementValueRepository,
) {

    operator fun invoke(
        measurements: List<MeasurementPropertyInputState>,
        entryId: Long,
    ) {
        val values = measurements.flatMap(MeasurementPropertyInputState::typeInputStates)
        val valuesFromBefore = measurementValueRepository.getByEntryId(entryId)
        values.forEach { (type, input) ->
            // TODO: Validate and normalize by unit
            val legacyId = valuesFromBefore.firstOrNull { it.typeId == type.id }?.id
            val normalized = input.toDoubleOrNull()
            if (normalized != null) {
                measurementValueRepository.update(
                    id = legacyId ?: measurementValueRepository.create(
                        value = normalized,
                        typeId = type.id,
                        entryId = entryId,
                    ),
                    value = normalized,
                )
            } else {
                val obsolete = valuesFromBefore.firstOrNull { it.typeId == type.id }
                if (obsolete != null) {
                    measurementValueRepository.deleteById(obsolete.id)
                }
            }
        }
    }
}