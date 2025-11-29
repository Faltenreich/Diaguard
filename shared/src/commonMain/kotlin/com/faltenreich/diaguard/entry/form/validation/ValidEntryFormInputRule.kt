package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ValidEntryFormInputRule : ValidationRule<EntryFormInput> {

    override fun check(input: EntryFormInput): ValidationResult<EntryFormInput> {
        return if (input.measurements.any { it.hasError }) {
            ValidationResult.Failure(input)
        } else {
            ValidationResult.Success(input)
        }
    }
}