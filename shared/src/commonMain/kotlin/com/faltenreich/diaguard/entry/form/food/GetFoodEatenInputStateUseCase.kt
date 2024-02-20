package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.eaten.FoodEatenInputState
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetFoodEatenInputStateUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
) {

    suspend operator fun invoke(entry: Entry?): List<FoodEatenInputState> = withContext(dispatcher) {
        entry ?: return@withContext emptyList()
        getFoodEatenForEntry(entry).map { foodEaten ->
            FoodEatenInputState(
                food = foodEaten.food,
                amountInGrams = foodEaten.amountInGrams,
            )
        }
    }
}