package com.faltenreich.diaguard.measurement.value

import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState

class CreateMeasurementValuesUseCase(
    private val repository: MeasurementValueRepository,
) {

    operator fun invoke(
        measurements: List<MeasurementCategoryInputState>,
        entryId: Long,
    ) {
        val values = measurements.flatMap(MeasurementCategoryInputState::propertyInputStates)
        val valuesFromBefore = repository.getByEntryId(entryId)
        values.forEach { (property, input) ->
            // TODO: Validate and normalize by unit
            val legacyId = valuesFromBefore.firstOrNull { it.propertyId == property.id }?.id
            val normalized = input.toDoubleOrNull()
            if (normalized != null) {
                if (legacyId != null) {
                    repository.update(
                        id = legacyId,
                        value = normalized,
                    )
                } else {
                    repository.create(
                        value = normalized,
                        propertyId = property.id,
                        entryId = entryId,
                    )
                }
            } else {
                val obsolete = valuesFromBefore.firstOrNull { it.propertyId == property.id }
                if (obsolete != null) {
                    repository.deleteById(obsolete.id)
                }
            }
        }
    }
}