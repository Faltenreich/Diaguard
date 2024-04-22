package com.faltenreich.diaguard.food.api

import kotlinx.coroutines.flow.Flow

interface FoodApi {

    fun search(query: String?, page: Int): Flow<List<FoodFromApi>>
}