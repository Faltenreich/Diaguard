package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository

class StoreMeasurementValuesUseCase(
    private val repository: MeasurementValueRepository,
    private val mapValue: MeasurementValueMapper,
) {

    operator fun invoke(
        measurements: List<MeasurementCategoryInputState>,
        entry: Entry.Local,
    ) {
        val values = measurements.flatMap(MeasurementCategoryInputState::propertyInputStates)
        val valuesFromBefore = repository.getByEntryId(entry.id)
        values.forEach { (property, input) ->
            val legacy = valuesFromBefore.firstOrNull { it.property.id == property.id }
            val normalized = mapValue(input, property)
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

    operator fun invoke(values: List<MeasurementValue.User>) {
        values.forEach(repository::create)
    }
}