package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_form_error
import diaguard.shared.generated.resources.entry_form_error_missing_input

class ValidEntryFormInputRule(
    private val localization: Localization = inject(),
) : ValidationRule<EntryFormInput> {

    override fun check(input: EntryFormInput): ValidationResult<EntryFormInput> {
        return if (hasNoInput(input)) {
            val error = localization.getString(Res.string.entry_form_error_missing_input)
            ValidationResult.Failure(input, error)
        } else if (hasError(input)) {
            val error = localization.getString(Res.string.entry_form_error)
            ValidationResult.Failure(input, error)
        } else {
            ValidationResult.Success(input)
        }
    }

    private fun hasNoInput(input: EntryFormInput): Boolean {
        return input.tags.isEmpty() &&
            input.note.isNullOrBlank() &&
            input.foodEaten.none { it.amountInGrams != null && it.amountInGrams > 0 } &&
            input.measurements.none { category ->
                category.propertyInputStates.none { property ->
                    property.input.isBlank()
                }
            }
    }

    private fun hasError(input: EntryFormInput): Boolean {
        return input.measurements.any { category ->
            category.error != null || category.propertyInputStates.any { property ->
                property.error != null
            }
        }
    }
}