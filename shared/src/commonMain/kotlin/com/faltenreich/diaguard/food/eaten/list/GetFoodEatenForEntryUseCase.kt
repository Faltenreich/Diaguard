package com.faltenreich.diaguard.food.eaten.list

import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetFoodEatenForEntryUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val foodEatenRepository: FoodEatenRepository,
) {

    suspend operator fun invoke(entry: Entry.Local): List<FoodEaten> = withContext(dispatcher) {
        foodEatenRepository.getByEntryId(entry.id)
    }
}