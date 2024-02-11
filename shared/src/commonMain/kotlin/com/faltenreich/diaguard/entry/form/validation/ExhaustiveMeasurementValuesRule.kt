package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.Rule

class ExhaustiveMeasurementValuesRule : Rule<EntryFormInput> {

    override fun check(input: EntryFormInput): Result<Unit> {
        // TODO: Check whether one value for every type is given
        // TODO: Add this info to data class of MeasurementType (isMandatory?)
        return Result.success(Unit)
    }
}