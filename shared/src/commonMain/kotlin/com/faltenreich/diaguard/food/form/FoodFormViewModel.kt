package com.faltenreich.diaguard.food.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.food.Food
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

    fun handleIntent(intent: FoodFormIntent) = viewModelScope.launch(dispatcher) {
        when (intent) {
            is FoodFormIntent.Submit -> submit()
            is FoodFormIntent.Delete -> delete()
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