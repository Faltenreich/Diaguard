package com.faltenreich.diaguard.entry.form.measurement

import com.faltenreich.diaguard.entry.form.EntryFormInput
import com.faltenreich.diaguard.shared.validation.ValidationResult
import com.faltenreich.diaguard.shared.validation.ValidationRule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateEntryFormInputUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val ruleForEntryFormInput: ValidationRule<EntryFormInput>,
    private val rulesForCategories: List<ValidationRule<MeasurementCategoryInputState>>,
    private val rulesForTypes: List<ValidationRule<MeasurementTypeInputState>>,
) {

    suspend operator fun invoke(
        input: EntryFormInput,
    ): ValidationResult<EntryFormInput> = withContext(dispatcher) {
        val result = input.copy(
            measurements = input.measurements.map { category ->
                val resultForCategory = validateCategory(category)
                category.copy(
                    error = (resultForCategory as? ValidationResult.Failure)?.error,
                    typeInputStates = category.typeInputStates.map { type ->
                        val resultForType = validateType(type)
                        type.copy(
                            error = (resultForType as? ValidationResult.Failure)?.error,
                        )
                    }
                )
            }
        )
        ruleForEntryFormInput.check(result)
    }

    private fun validateCategory(
        input: MeasurementCategoryInputState,
    ): ValidationResult<MeasurementCategoryInputState> {
        return rulesForCategories
            .map { it.check(input) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(input)
    }

    private fun validateType(
        input: MeasurementTypeInputState,
    ): ValidationResult<MeasurementTypeInputState> {
        return rulesForTypes
            .map { it.check(input) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(input)
    }
}