package com.faltenreich.diaguard.food.form

import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListScreen
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.core.di.inject
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
    createInput: CreateFoodFormInputUseCase = inject(),
    private val validateInput: ValidateFoodInputUseCase = inject(),
    private val storeFood: StoreFoodUseCase = inject(),
    private val deleteFood: DeleteFoodUseCase = inject(),
    private val pushScreen: PushScreenUseCase = inject(),
    private val popScreen: PopScreenUseCase = inject(),
) : ViewModel<FoodFormState, FoodFormIntent, Unit>() {

    private val food = foodId?.let(getFoodById::invoke)
    private val input = MutableStateFlow(createInput(food))
    private val error = MutableStateFlow<String?>(null)
    private val deleteDialog = MutableStateFlow<FoodFormState.DeleteDialog?>(null)

    override val state = combine(
        flowOf(food),
        input,
        error,
        deleteDialog,
        ::FoodFormState,
    )

    init {
        scope.launch {
            input.collectLatest {
                // Reset error on any input
                error.update { null }
            }
        }
    }

    override suspend fun handleIntent(intent: FoodFormIntent) {
        when (intent) {
            is FoodFormIntent.SetInput -> input.update { intent.input }
            is FoodFormIntent.SetNutrient -> setNutrient(intent.data)
            is FoodFormIntent.OpenFoodEaten -> pushScreen(FoodEatenListScreen(intent.food))
            is FoodFormIntent.Submit -> submit()
            is FoodFormIntent.Delete -> delete(intent.needsConfirmation)
            is FoodFormIntent.CloseDeleteDialog -> deleteDialog.update { null }
        }
    }

    private fun setNutrient(nutrientData: FoodNutrientData) = with(nutrientData) {
        input.update { input ->
            when (nutrient) {
                FoodNutrient.CARBOHYDRATES -> input.copy(carbohydrates = per100g)
                FoodNutrient.SUGAR -> input.copy(sugar = per100g)
                FoodNutrient.ENERGY -> input.copy(energy = per100g)
                FoodNutrient.FAT -> input.copy(fat = per100g)
                FoodNutrient.FAT_SATURATED -> input.copy(fatSaturated = per100g)
                FoodNutrient.FIBER -> input.copy(fiber = per100g)
                FoodNutrient.PROTEINS -> input.copy(proteins = per100g)
                FoodNutrient.SALT -> input.copy(salt = per100g)
                FoodNutrient.SODIUM -> input.copy(sodium = per100g)
            }
        }
    }

    private suspend fun submit() = with(input.value) {
        val input = Food.Input(
            name = name,
            brand = brand,
            ingredients = ingredients,
            labels = labels,
            carbohydrates = carbohydrates,
            energy = energy,
            fat = fat,
            fatSaturated = fatSaturated,
            fiber = fiber,
            proteins = proteins,
            salt = salt,
            sodium = sodium,
            sugar = sugar,
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

    private suspend fun delete(needsConfirmation: Boolean) {
        val food = food
        if (food != null) {
            if (needsConfirmation) {
                deleteDialog.update { FoodFormState.DeleteDialog }
            } else {
                deleteFood(food)
                popScreen()
            }
        } else {
            popScreen()
        }
    }
}