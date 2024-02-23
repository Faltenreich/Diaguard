package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule

class ValidEntryFormInputRule(
    private val localization: Localization = inject(),
) : ValidationRule<EntryFormInput> {

    override fun check(input: EntryFormInput): ValidationResult<EntryFormInput> {
        val isValid = input.measurements.all { property ->
            property.error == null && property.typeInputStates.all { type ->
                type.error == null
            }
        }
        return if (isValid) {
            ValidationResult.Success(input)
        } else {
            val error = localization.getString(MR.strings.entry_form_error)
            ValidationResult.Failure(input, error)
        }
    }
}