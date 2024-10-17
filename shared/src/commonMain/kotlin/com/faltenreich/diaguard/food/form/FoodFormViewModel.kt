package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.navigation.bar.snack.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.primitive.NumberFormatter
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodFormViewModel(
    foodId: Long?,
    getFoodById: GetFoodByIdUseCase = inject(),
    private val validateInput: ValidateFoodInputUseCase = inject(),
    private val storeFood: StoreFoodUseCase = inject(),
    private val deleteFood: DeleteFoodUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showSnackbar: ShowSnackbarUseCase = inject(),
    private val formatNumber: NumberFormatter = inject(),
    private val localization: Localization = inject(),
) : ViewModel<Nothing, FoodFormIntent, Unit>() {

    val food = foodId?.let(getFoodById::invoke)

    override val state: Flow<Nothing>
        get() = throw UnsupportedOperationException()

    var name: String by mutableStateOf(food?.name ?: "")
    var brand: String by mutableStateOf(food?.brand ?: "")
    var ingredients: String by mutableStateOf(food?.ingredients ?: "")
    var labels: String by mutableStateOf(food?.labels ?: "")
    var carbohydrates: String by mutableStateOf(food?.carbohydrates?.let(::formatNutrient) ?: "")
    var energy: String by mutableStateOf(food?.energy?.let(::formatNutrient) ?: "")
    var fat: String by mutableStateOf(food?.fat?.let(::formatNutrient) ?: "")
    var fatSaturated: String by mutableStateOf(food?.fatSaturated?.let(::formatNutrient) ?: "")
    var fiber: String by mutableStateOf(food?.fiber?.let(::formatNutrient) ?: "")
    var proteins: String by mutableStateOf(food?.proteins?.let(::formatNutrient) ?: "")
    var salt: String by mutableStateOf(food?.salt?.let(::formatNutrient) ?: "")
    var sodium: String by mutableStateOf(food?.sodium?.let(::formatNutrient) ?: "")
    var sugar: String by mutableStateOf(food?.sugar?.let(::formatNutrient) ?: "")

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

    override suspend fun handleIntent(intent: FoodFormIntent) {
        when (intent) {
            is FoodFormIntent.EditNutrient -> editNutrient(intent.data)
            is FoodFormIntent.OpenFoodEaten -> navigateToScreen(FoodEatenListScreen(intent.food))
            is FoodFormIntent.Submit -> submit()
            is FoodFormIntent.Delete -> delete()
        }
    }

    private fun formatNutrient(nutrient: Double): String {
        return formatNumber(
            number = nutrient,
            scale = SCALE,
            locale = localization.getLocale(),
        )
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
        val food = food?.copy(
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

        when (val result = validateInput(food)) {
            is ValidationResult.Success -> {
                storeFood(result.data)
                navigateBack()
            }
            is ValidationResult.Failure -> {
                showSnackbar(result.error)
            }
        }
    }

    private suspend fun delete() {
        val food = food ?: return
        deleteFood(food)
        navigateBack()
    }

    companion object {

        private const val SCALE = 3
    }
}