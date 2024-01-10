package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetFoodEatenInputDataUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
) {

    suspend operator fun invoke(entry: Entry?): List<FoodEatenInputData> = withContext(dispatcher) {
        entry ?: return@withContext emptyList()
        getFoodEatenForEntry(entry).map { foodEaten ->
            FoodEatenInputData(
                food = foodEaten.food,
                amountInGrams = foodEaten.amountInGrams,
            )
        }
    }
}