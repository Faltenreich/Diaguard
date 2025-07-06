package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.NumberFormatter
import com.faltenreich.diaguard.shared.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodFormViewModel(
    foodId: Long?,
    getFoodById: GetFoodByIdUseCase = inject(),
    private val validateInput: ValidateFoodInputUseCase = inject(),
    private val storeFood: StoreFoodUseCase = inject(),
    private val deleteFood: DeleteFoodUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
    private val formatNumber: NumberFormatter = inject(),
) : ViewModel<FoodFormState, FoodFormIntent, Unit>() {

    private val food = foodId?.let(getFoodById::invoke)
    private val error = MutableStateFlow<String?>(null)

    private val name = MutableStateFlow(food?.name ?: "")
    private val brand = MutableStateFlow(food?.brand ?: "")
    private val ingredients = MutableStateFlow(food?.ingredients ?: "")
    private val labels = MutableStateFlow(food?.labels ?: "")
    private val carbohydrates = MutableStateFlow(food?.carbohydrates?.let(::formatNutrient) ?: "")
    private val energy = MutableStateFlow(food?.energy?.let(::formatNutrient) ?: "")
    private val fat = MutableStateFlow(food?.fat?.let(::formatNutrient) ?: "")
    private val fatSaturated = MutableStateFlow(food?.fatSaturated?.let(::formatNutrient) ?: "")
    private val fiber = MutableStateFlow(food?.fiber?.let(::formatNutrient) ?: "")
    private val proteins = MutableStateFlow(food?.proteins?.let(::formatNutrient) ?: "")
    private val salt = MutableStateFlow(food?.salt?.let(::formatNutrient) ?: "")
    private val sodium = MutableStateFlow(food?.sodium?.let(::formatNutrient) ?: "")
    private val sugar = MutableStateFlow(food?.sugar?.let(::formatNutrient) ?: "")

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
                    FoodNutrient.CARBOHYDRATES -> carbohydrates.value
                    FoodNutrient.SUGAR -> sugar.value
                    FoodNutrient.ENERGY -> energy.value
                    FoodNutrient.FAT -> fat.value
                    FoodNutrient.FAT_SATURATED -> fatSaturated.value
                    FoodNutrient.FIBER -> fiber.value
                    FoodNutrient.PROTEINS -> proteins.value
                    FoodNutrient.SALT -> salt.value
                    FoodNutrient.SODIUM -> sodium.value
                },
                isLast = index == nutrients.size - 1,
            )
        }

    override val state = combine(
        flowOf(food),
        name,
        brand,
        ingredients,
        labels,
        error,
        ::FoodFormState,
    )

    init {
        scope.launch {
            combine(
                snapshotFlow { name },
                snapshotFlow { carbohydrates },
            ) { /* Observe any user input */ }.collectLatest {
                error.update { null }
            }
        }
    }

    override suspend fun handleIntent(intent: FoodFormIntent) {
        when (intent) {
            is FoodFormIntent.SetName -> name.update { intent.name }
            is FoodFormIntent.SetBrand -> brand.update { intent.brand }
            is FoodFormIntent.SetIngredients -> ingredients.update { intent.ingredients }
            is FoodFormIntent.SetLabels -> labels.update { intent.labels }
            is FoodFormIntent.EditNutrient -> editNutrient(intent.data)
            is FoodFormIntent.OpenFoodEaten -> pushScreen(FoodEatenListScreen(intent.food))
            is FoodFormIntent.Submit -> submit()
            is FoodFormIntent.Delete -> delete()
        }
    }

    private fun formatNutrient(nutrient: Double): String {
        return formatNumber(
            number = nutrient,
            scale = SCALE,
        )
    }

    private fun editNutrient(data: FoodNutrientData) {
        when (data.nutrient) {
            FoodNutrient.CARBOHYDRATES -> carbohydrates.update { data.per100g }
            FoodNutrient.SUGAR -> sugar.update { data.per100g }
            FoodNutrient.ENERGY -> energy.update { data.per100g }
            FoodNutrient.FAT -> fat.update { data.per100g }
            FoodNutrient.FAT_SATURATED -> fatSaturated.update {  data.per100g }
            FoodNutrient.FIBER -> fiber.update {  data.per100g }
            FoodNutrient.PROTEINS -> proteins.update {  data.per100g }
            FoodNutrient.SALT -> salt.update {  data.per100g }
            FoodNutrient.SODIUM -> sodium.update {  data.per100g }
        }
    }

    private fun submit() = scope.launch {
        val input = Food.Input(
            name = name.value,
            brand = brand.value,
            ingredients = ingredients.value,
            labels = labels.value,
            carbohydrates = carbohydrates.value,
            energy = energy.value,
            fat = fat.value,
            fatSaturated = fatSaturated.value,
            fiber = fiber.value,
            proteins = proteins.value,
            salt = salt.value,
            sodium = sodium.value,
            sugar = sugar.value,
        )

        when (val result = validateInput(input, food)) {
            is ValidationResult.Success -> {
                storeFood(result.data)
                popScreen()
            }
            is ValidationResult.Failure -> {
                error.update { result.error }
            }
        }
    }

    private suspend fun delete() {
        val food = food ?: return
        deleteFood(food)
        popScreen()
    }

    companion object {

        private const val SCALE = 3
    }
}