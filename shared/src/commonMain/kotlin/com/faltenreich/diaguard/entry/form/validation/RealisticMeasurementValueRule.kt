package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.validation.Rule

class RealisticMeasurementValueRule(
    private val measurementValueFormatter: MeasurementValueFormatter = inject(),
) : Rule<EntryFormInput> {

    override fun check(input: EntryFormInput): Result<Unit> {
        // TODO: Check whether values are within MeasurementType.minimumValue and .maximumValue
        val violations = input.measurements.flatMap {
            it.typeInputStates.filter {
                it.input
                false
            }
        }
        return if (violations.isEmpty()) {
            Result.success(Unit)
        } else {
            return Result.failure(RealisticMeasurementValueException(violations))
        }
    }
}