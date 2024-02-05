package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.Rule

class MeasurementValueIsWithinRangeRule : Rule<EntryFormInput> {

    override fun check(input: EntryFormInput): Result<Unit> {
        // TODO: Check whether values are within MeasurementType.minimumValue and .maximumValue
        return Result.success(Unit)
    }
}