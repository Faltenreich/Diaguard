package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.data.entry.Entry
import com.faltenreich.diaguard.data.food.eaten.FoodEaten
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import kotlinx.coroutines.flow.Flow

class GetFoodEatenForEntryUseCase(
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(entry: Entry.Local): Flow<List<FoodEaten>> {
        return foodEatenRepository.observeByEntryId(entry.id)
    }
}