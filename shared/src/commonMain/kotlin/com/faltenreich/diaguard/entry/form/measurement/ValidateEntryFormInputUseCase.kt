package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.architecture.either.ValidationResult
import com.faltenreich.diaguard.architecture.either.ValidationRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateEntryFormInputUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val ruleForEntryFormInput: ValidationRule<EntryFormInput>,
    private val rulesForProperties: List<ValidationRule<MeasurementPropertyInputState>>,
) {

    suspend operator fun invoke(
        input: EntryFormInput,
    ): ValidationResult<EntryFormInput> = withContext(dispatcher) {
        val result = input.copy(
            measurements = input.measurements.map { category ->
                category.copy(
                    propertyInputStates = category.propertyInputStates.map { property ->
                        val resultForProperty = validateProperty(property)
                        property.copy(
                            error = (resultForProperty as? ValidationResult.Failure)?.error,
                        )
                    }
                )
            }
        )
        ruleForEntryFormInput.check(result)
    }

    private fun validateProperty(
        input: MeasurementPropertyInputState,
    ): ValidationResult<MeasurementPropertyInputState> {
        return rulesForProperties
            .map { it.check(input) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(input)
    }
}