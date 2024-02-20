package com.faltenreich.diaguard.entry.form.measurement

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateEntryFormInputUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        input: List<MeasurementPropertyInputState>,
    ): Result<List<MeasurementPropertyInputState>> = withContext(dispatcher) {
        // TODO: Call validation chain and update input
        Result.success(input)
    }
}