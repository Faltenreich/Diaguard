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

    suspend operator fun invoke(
        input: Food.Input,
        existing: Food.Local?,
    ): ValidationResult<Food> = withContext(dispatcher) {
        val food = with(input) {
            existing?.copy(
                name = name,
                brand = brand.takeIf(String::isNotBlank),
                ingredients = ingredients.takeIf(String::isNotBlank),
                labels = labels.takeIf(String::isNotBlank),
                carbohydrates = carbohydrates.toDoubleOrNull() ?: -1.0,
                energy = energy.toDoubleOrNull()?.takeIf { it > 0 },
                fat = fat.toDoubleOrNull()?.takeIf { it > 0 },
                fatSaturated = fatSaturated.toDoubleOrNull()?.takeIf { it > 0 },
                fiber = fiber.toDoubleOrNull()?.takeIf { it > 0 },
                proteins = proteins.toDoubleOrNull()?.takeIf { it > 0 },
                salt = salt.toDoubleOrNull()?.takeIf { it > 0 },
                sodium = sodium.toDoubleOrNull()?.takeIf { it > 0 },
                sugar = sugar.toDoubleOrNull()?.takeIf { it > 0 },
            ) ?: Food.User(
                name = name,
                brand = brand.takeIf(String::isNotBlank),
                ingredients = ingredients.takeIf(String::isNotBlank),
                labels = labels.takeIf(String::isNotBlank),
                carbohydrates = carbohydrates.toDoubleOrNull() ?: -1.0,
                energy = energy.toDoubleOrNull()?.takeIf { it > 0 },
                fat = fat.toDoubleOrNull()?.takeIf { it > 0 },
                fatSaturated = fatSaturated.toDoubleOrNull()?.takeIf { it > 0 },
                fiber = fiber.toDoubleOrNull()?.takeIf { it > 0 },
                proteins = proteins.toDoubleOrNull()?.takeIf { it > 0 },
                salt = salt.toDoubleOrNull()?.takeIf { it > 0 },
                sodium = sodium.toDoubleOrNull()?.takeIf { it > 0 },
                sugar = sugar.toDoubleOrNull()?.takeIf { it > 0 },
            )
        }
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