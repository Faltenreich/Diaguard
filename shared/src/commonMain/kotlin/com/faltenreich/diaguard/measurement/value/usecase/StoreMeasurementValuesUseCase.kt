package com.faltenreich.diaguard.measurement.value.usecase

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository

class StoreMeasurementValuesUseCase(private val repository: MeasurementValueRepository) {

    operator fun invoke(
        measurements: List<MeasurementCategoryInputState>,
        entry: Entry.Local,
    ) {
        val values = measurements.flatMap(MeasurementCategoryInputState::propertyInputStates)
        val valuesFromBefore = repository.getByEntryId(entry.id)
        values.forEach { (property, input) ->
            // TODO: Validate and normalize by unit
            val legacy = valuesFromBefore.firstOrNull { it.property.id == property.id }
            val normalized = input.toDoubleOrNull()
            if (normalized != null) {
                if (legacy != null) {
                    repository.update(legacy.copy(value = normalized))
                } else {
                    repository.create(
                        MeasurementValue.User(
                            value = normalized,
                            property = property,
                            entry = entry,
                        )
                    )
                }
            } else {
                val obsolete = valuesFromBefore.firstOrNull { it.property.id == property.id }
                if (obsolete != null) {
                    repository.delete(obsolete)
                }
            }
        }
    }
}