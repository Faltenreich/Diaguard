package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.shared.data.PagingPage

interface FoodApi {

    suspend fun search(query: String?, page: PagingPage): List<FoodFromApi>
}