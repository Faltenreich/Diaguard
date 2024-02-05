package com.faltenreich.diaguard.entry.form.validation

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.Rule

class EntryFormHasInputRule : Rule<EntryFormInput> {

    override fun check(input: EntryFormInput): Result<Unit> {
        return if (input.measurements.isNotEmpty()
            || input.tags.isNotEmpty()
            || input.note?.isNotBlank() == true
        ) {
            Result.success(Unit)
        } else {
            Result.failure(EntryFormIsMissingInputException())
        }
    }
}