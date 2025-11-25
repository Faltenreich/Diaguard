package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.screen.NavigateToUseCase
import kotlinx.coroutines.flow.map

class FoodEatenListViewModel(
    foodId: Long,
    getFoodById: GetFoodByIdUseCase = inject(),
    getFoodEaten: GetFoodEatenForFoodUseCase = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
) : ViewModel<FoodEatenListState, FoodEatenListIntent, Unit>() {

    val food: Food.Local = checkNotNull(getFoodById(foodId))

    override val state = getFoodEaten(food).map { results ->
        when {
            results.isEmpty() -> FoodEatenListState.Empty
            else -> FoodEatenListState.NonEmpty(results)
        }
    }

    override suspend fun handleIntent(intent: FoodEatenListIntent) {
        when (intent) {
            is FoodEatenListIntent.CreateEntry -> navigateTo(NavigationTarget.EntryForm(foodId = food.id))
            is FoodEatenListIntent.OpenEntry -> navigateTo(NavigationTarget.EntryForm(entryId = intent.entry.id))
        }
    }
}