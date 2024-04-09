package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState

class CreateMeasurementValuesUseCase(
    private val measurementValueRepository: MeasurementValueRepository,
) {

    operator fun invoke(
        measurements: List<MeasurementCategoryInputState>,
        entryId: Long,
    ) {
        val values = measurements.flatMap(MeasurementCategoryInputState::propertyInputStates)
        val valuesFromBefore = measurementValueRepository.getByEntryId(entryId)
        values.forEach { (property, input) ->
            // TODO: Validate and normalize by unit
            val legacyId = valuesFromBefore.firstOrNull { it.propertyId == property.id }?.id
            val normalized = input.toDoubleOrNull()
            if (normalized != null) {
                measurementValueRepository.update(
                    id = legacyId ?: measurementValueRepository.create(
                        value = normalized,
                        propertyId = property.id,
                        entryId = entryId,
                    ),
                    value = normalized,
                )
            } else {
                val obsolete = valuesFromBefore.firstOrNull { it.propertyId == property.id }
                if (obsolete != null) {
                    measurementValueRepository.deleteById(obsolete.id)
                }
            }
        }
    }
}