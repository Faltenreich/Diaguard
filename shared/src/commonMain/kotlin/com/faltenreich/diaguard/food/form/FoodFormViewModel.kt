package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.nutrient.FoodNutrient
import com.faltenreich.diaguard.food.nutrient.FoodNutrientData
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class FoodFormViewModel(
    food: Food?,
    private val dispatcher: CoroutineDispatcher = inject(),
    private val createFood: CreateFoodUseCase = inject(),
) : ViewModel() {

    private val id: Long? = food?.id

    var name: String by mutableStateOf(food?.name ?: "")
    var brand: String by mutableStateOf(food?.brand ?: "")
    var ingredients: String by mutableStateOf(food?.ingredients ?: "")
    var labels: String by mutableStateOf(food?.labels ?: "")
    var carbohydrates: Double? by mutableStateOf(food?.carbohydrates)
    var energy: Double? by mutableStateOf(food?.energy)
    var fat: Double? by mutableStateOf(food?.fat)
    var fatSaturated: Double? by mutableStateOf(food?.fatSaturated)
    var fiber: Double? by mutableStateOf(food?.fiber)
    var proteins: Double? by mutableStateOf(food?.proteins)
    var salt: Double? by mutableStateOf(food?.salt)
    var sodium: Double? by mutableStateOf(food?.sodium)
    var sugar: Double? by mutableStateOf(food?.sugar)

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

    fun handleIntent(intent: FoodFormIntent) = viewModelScope.launch(dispatcher) {
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
        val carbohydrates = carbohydrates ?: return
        createFood(
            id = id,
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
    }

    private fun delete() = Unit
}