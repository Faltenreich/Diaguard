package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.food.Food
import kotlinx.coroutines.flow.Flow

interface FoodApi {

    fun search(query: String?, page: Int): Flow<List<Food>>
}