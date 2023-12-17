package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.shared.architecture.FormViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.NumberFormatter

class FoodFormViewModel(
    food: Food?,
    private val createFood: CreateFoodUseCase = inject(),
    private val deleteFood: DeleteFoodUseCase = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : FormViewModel<FoodFormIntent>() {

    private val id: Long? = food?.id

    var name: String by mutableStateOf(food?.name ?: "")
    var brand: String by mutableStateOf(food?.brand ?: "")
    var ingredients: String by mutableStateOf(food?.ingredients ?: "")
    var labels: String by mutableStateOf(food?.labels ?: "")
    var carbohydrates: String by mutableStateOf(food?.carbohydrates?.let(numberFormatter::format) ?: "")
    var energy: String by mutableStateOf(food?.energy?.let(numberFormatter::format) ?: "")
    var fat: String by mutableStateOf(food?.fat?.let(numberFormatter::format) ?: "")
    var fatSaturated: String by mutableStateOf(food?.fatSaturated?.let(numberFormatter::format) ?: "")
    var fiber: String by mutableStateOf(food?.fiber?.let(numberFormatter::format) ?: "")
    var proteins: String by mutableStateOf(food?.proteins?.let(numberFormatter::format) ?: "")
    var salt: String by mutableStateOf(food?.salt?.let(numberFormatter::format) ?: "")
    var sodium: String by mutableStateOf(food?.sodium?.let(numberFormatter::format) ?: "")
    var sugar: String by mutableStateOf(food?.sugar?.let(numberFormatter::format) ?: "")

    private val nutrients = listOf(
        FoodNutrient.CARBOHYDRATES,
        FoodNutrient.SUGAR,
        FoodNutrient.ENERGY,
        FoodNutrient.FAT,
        FoodNutrient.FAT_SATURATED,
        FoodNutrient.FIBER,
        FoodNutrient.PROTEINS,
        FoodNutrient.SALT,
        FoodNutrient.SODIUM,
    )

    val nutrientData: List<FoodNutrientData>
        get() = nutrients.mapIndexed { index, nutrient ->
            FoodNutrientData(
                nutrient = nutrient,
                per100g = when (nutrient) {
                    FoodNutrient.CARBOHYDRATES -> carbohydrates
                    FoodNutrient.SUGAR -> sugar
                    FoodNutrient.ENERGY -> energy
                    FoodNutrient.FAT -> fat
                    FoodNutrient.FAT_SATURATED -> fatSaturated
                    FoodNutrient.FIBER -> fiber
                    FoodNutrient.PROTEINS -> proteins
                    FoodNutrient.SALT -> salt
                    FoodNutrient.SODIUM -> sodium
                },
                isLast = index == nutrients.size - 1,
            )
        }

    override fun onIntent(intent: FoodFormIntent) {
        when (intent) {
            is FoodFormIntent.EditNutrient -> editNutrient(intent.data)
            is FoodFormIntent.Submit -> submit()
            is FoodFormIntent.Delete -> delete()
        }
    }

    private fun editNutrient(data: FoodNutrientData) {
        when (data.nutrient) {
            FoodNutrient.CARBOHYDRATES -> carbohydrates = data.per100g
            FoodNutrient.SUGAR -> sugar = data.per100g
            FoodNutrient.ENERGY -> energy = data.per100g
            FoodNutrient.FAT -> fat = data.per100g
            FoodNutrient.FAT_SATURATED -> fatSaturated = data.per100g
            FoodNutrient.FIBER -> fiber = data.per100g
            FoodNutrient.PROTEINS -> proteins = data.per100g
            FoodNutrient.SALT -> salt = data.per100g
            FoodNutrient.SODIUM -> sodium = data.per100g
        }
    }

    private fun submit() {
        createFood(
            id = id,
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates.toDoubleOrNull() ?: 0.0,
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

    private fun delete() {
        val id = id ?: return
        deleteFood(id)
    }
}