package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import kotlinx.coroutines.flow.Flow

class GetFoodEatenForEntryUseCase(
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(entry: Entry): Flow<List<FoodEaten>> {
        return foodEatenRepository.observeByEntryId(entry.id)
    }
}