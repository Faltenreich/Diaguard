package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.shared.data.PagingPage
import kotlinx.coroutines.flow.Flow

interface FoodApi {

    fun search(query: String?, page: PagingPage): Flow<List<FoodFromApi>>
}