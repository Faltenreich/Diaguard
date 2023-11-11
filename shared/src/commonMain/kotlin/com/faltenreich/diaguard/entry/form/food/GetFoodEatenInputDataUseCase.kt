package com.faltenreich.diaguard.entry.form.food

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.detail.eaten.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.food.eaten.FoodEatenInputData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetFoodEatenInputDataUseCase(
    private val getFoodEatenForEntry: GetFoodEatenForEntryUseCase,
) {

    operator fun invoke(entry: Entry?): Flow<List<FoodEatenInputData>> {
        entry ?: return flowOf(emptyList())
        return getFoodEatenForEntry(entry).map { foodEatenList ->
            foodEatenList.map { foodEaten ->
                FoodEatenInputData(
                    food = foodEaten.food,
                    amountInGrams = foodEaten.amountInGrams,
                )
            }
        }
    }
}