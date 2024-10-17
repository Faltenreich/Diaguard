package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.food.Food
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

    suspend operator fun invoke(food: Food): ValidationResult<Food> = withContext(dispatcher) {
        if (food.name.isNotBlank() && food.carbohydrates >= 0) {
            ValidationResult.Success(food)
        } else {
            ValidationResult.Failure(
                data = food,
                error = localization.getString(Res.string.food_form_missing_input),
            )
        }
    }
}