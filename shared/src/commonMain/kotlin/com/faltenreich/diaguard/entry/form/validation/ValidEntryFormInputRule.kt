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
        return if (hasNoInput(input)) {
            val error = localization.getString(MR.strings.entry_form_error_missing_input)
            ValidationResult.Failure(input, error)
        } else if (hasError(input)) {
            val error = localization.getString(MR.strings.entry_form_error)
            ValidationResult.Failure(input, error)
        } else {
            ValidationResult.Success(input)
        }
    }

    private fun hasNoInput(input: EntryFormInput): Boolean {
        return input.tags.isEmpty() &&
            input.note.isNullOrBlank() &&
            input.measurements.none { property ->
                property.typeInputStates.none { type ->
                    type.input.isBlank()
                }
            }
    }

    private fun hasError(input: EntryFormInput): Boolean {
        return input.measurements.any { property ->
            property.error != null || property.typeInputStates.any { type ->
                type.error != null
            }
        }
    }
}