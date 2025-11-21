package com.faltenreich.diaguard.data.food.api

import com.faltenreich.diaguard.view.paging.PagingPage

interface FoodApi {

    suspend fun search(query: String?, page: PagingPage): List<FoodFromApi>
}