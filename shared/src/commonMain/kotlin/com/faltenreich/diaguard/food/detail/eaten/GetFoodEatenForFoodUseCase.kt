package com.faltenreich.diaguard.food.detail.eaten

import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.eaten.FoodEaten
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import kotlinx.coroutines.flow.Flow

class GetFoodEatenForFoodUseCase(
    private val foodEatenRepository: FoodEatenRepository,
) {

    operator fun invoke(food: Food): Flow<List<FoodEaten>> {
        return foodEatenRepository.observeByFoodId(food.id)
    }
}