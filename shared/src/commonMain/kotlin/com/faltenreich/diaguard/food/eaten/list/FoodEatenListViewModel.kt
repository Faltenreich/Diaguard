package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.map

class FoodEatenListViewModel(
    foodId: Long,
    getFoodById: GetFoodByIdUseCase = inject(),
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<FoodEatenListState, FoodEatenListIntent, Unit>() {

    val food: Food.Local = requireNotNull(getFoodById(foodId))

    override val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListState.Empty
            else -> FoodEatenListState.NonEmpty(results)
        }
    }

    override suspend fun handleIntent(intent: FoodEatenListIntent) {
        when (intent) {
            is FoodEatenListIntent.CreateEntry -> navigateToScreen(EntryFormScreen(food = food))
            is FoodEatenListIntent.OpenEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}