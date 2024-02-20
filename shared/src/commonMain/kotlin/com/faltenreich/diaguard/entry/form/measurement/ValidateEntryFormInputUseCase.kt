package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateEntryFormInputUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        input: EntryFormInput,
    ): ValidationResult<EntryFormInput> = withContext(dispatcher) {
        // TODO: Call validation chain and update input
        ValidationResult.Success(input)
    }
}