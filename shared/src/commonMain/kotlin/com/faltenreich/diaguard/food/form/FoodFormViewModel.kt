package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.FoodEatenListScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodFormViewModel(
    private val food: Food.Local?,
    private val validateInput: ValidateFoodInputUseCase = inject(),
    private val createFood: CreateFoodUseCase = inject(),
    private val updateFood: UpdateFoodUseCase = inject(),
    private val deleteFood: DeleteFoodUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showSnackbar: ShowSnackbarUseCase = inject(),
    private val numberFormatter: NumberFormatter = inject(),
) : ViewModel<Nothing, FoodFormIntent, Unit>() {

    override val state: Flow<Nothing>
        get() = throw UnsupportedOperationException()

    var name: String by mutableStateOf(food?.name ?: "")
    var brand: String by mutableStateOf(food?.brand ?: "")
    var ingredients: String by mutableStateOf(food?.ingredients ?: "")
    var labels: String by mutableStateOf(food?.labels ?: "")
    var carbohydrates: String by mutableStateOf(food?.carbohydrates?.let(numberFormatter::invoke) ?: "")
    var energy: String by mutableStateOf(food?.energy?.let(numberFormatter::invoke) ?: "")
    var fat: String by mutableStateOf(food?.fat?.let(numberFormatter::invoke) ?: "")
    var fatSaturated: String by mutableStateOf(food?.fatSaturated?.let(numberFormatter::invoke) ?: "")
    var fiber: String by mutableStateOf(food?.fiber?.let(numberFormatter::invoke) ?: "")
    var proteins: String by mutableStateOf(food?.proteins?.let(numberFormatter::invoke) ?: "")
    var salt: String by mutableStateOf(food?.salt?.let(numberFormatter::invoke) ?: "")
    var sodium: String by mutableStateOf(food?.sodium?.let(numberFormatter::invoke) ?: "")
    var sugar: String by mutableStateOf(food?.sugar?.let(numberFormatter::invoke) ?: "")

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

    override fun handleIntent(intent: FoodFormIntent) {
        when (intent) {
            is FoodFormIntent.EditNutrient -> editNutrient(intent.data)
            is FoodFormIntent.OpenFoodEaten -> navigateToScreen(FoodEatenListScreen(intent.food))
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

    private fun submit() = scope.launch {
        val foodInput = FoodInput(
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates.toDoubleOrNull()?.takeIf { it > 0 },
            energy = energy.toDoubleOrNull()?.takeIf { it > 0 },
            fat = fat.toDoubleOrNull()?.takeIf { it > 0 },
            fatSaturated = fatSaturated.toDoubleOrNull()?.takeIf { it > 0 },
            fiber = fiber.toDoubleOrNull()?.takeIf { it > 0 },
            proteins = proteins.toDoubleOrNull()?.takeIf { it > 0 },
            salt = salt.toDoubleOrNull()?.takeIf { it > 0 },
            sodium = sodium.toDoubleOrNull()?.takeIf { it > 0 },
            sugar = sugar.toDoubleOrNull()?.takeIf { it > 0 },
        )

        when (val result = validateInput(foodInput)) {
            is ValidationResult.Success -> {
                // TODO: Remove force unwraps
                when (val food = food) {
                    null -> with(foodInput) {
                        val new = Food.User(
                            name = name!!,
                            brand = brand,
                            ingredients = ingredients,
                            labels = labels,
                            carbohydrates = carbohydrates!!,
                            energy = energy,
                            fat = fat,
                            fatSaturated = fatSaturated,
                            fiber = fiber,
                            proteins = proteins,
                            salt = salt,
                            sodium = sodium,
                            sugar = sugar,
                        )
                        createFood(new)
                    }
                    else -> with(foodInput) {
                        val update = food.copy(
                            name = name!!,
                            brand = brand,
                            ingredients = ingredients,
                            labels = labels,
                            carbohydrates = carbohydrates!!,
                            energy = energy,
                            fat = fat,
                            fatSaturated = fatSaturated,
                            fiber = fiber,
                            proteins = proteins,
                            salt = salt,
                            sodium = sodium,
                            sugar = sugar,
                        )
                        updateFood(update)
                    }
                }
                navigateBack()
            }
            is ValidationResult.Failure -> showSnackbar(result.error)
        }
    }

    private fun delete() {
        val id = food?.id ?: return
        deleteFood(id)
        navigateBack()
    }
}