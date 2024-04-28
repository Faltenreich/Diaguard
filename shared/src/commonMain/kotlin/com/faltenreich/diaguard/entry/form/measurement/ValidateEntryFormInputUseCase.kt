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
    private val rulesForProperties: List<ValidationRule<MeasurementPropertyInputState>>,
) {

    suspend operator fun invoke(
        input: EntryFormInput,
    ): ValidationResult<EntryFormInput> = withContext(dispatcher) {
        // FIXME: Returns failure if only food eaten is added
        val result = input.copy(
            measurements = input.measurements.map { category ->
                val resultForCategory = validateCategory(category)
                category.copy(
                    error = (resultForCategory as? ValidationResult.Failure)?.error,
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

    private fun validateCategory(
        input: MeasurementCategoryInputState,
    ): ValidationResult<MeasurementCategoryInputState> {
        return rulesForCategories
            .map { it.check(input) }
            .firstOrNull { it is ValidationResult.Failure }
            ?: ValidationResult.Success(input)
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