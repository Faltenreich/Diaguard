package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateFoodInputUseCase(
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(
        input: FoodInput,
    ): ValidationResult<FoodInput> = withContext(dispatcher) {
        if (input.name != null && input.carbohydrates != null) {
            ValidationResult.Success(input)
        } else {
            ValidationResult.Failure(input, "TODO")
        }
    }
}