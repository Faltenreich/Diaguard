package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.validation.ValidationResult
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_form_missing_input
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ValidateFoodInputUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val localization: Localization,
) {

    suspend operator fun invoke(
        input: FoodInput,
    ): ValidationResult<FoodInput> = withContext(dispatcher) {
        if (input.name?.isNotBlank() == true && input.carbohydrates != null) {
            ValidationResult.Success(input)
        } else {
            ValidationResult.Failure(input, localization.getString(Res.string.food_form_missing_input))
        }
    }
}