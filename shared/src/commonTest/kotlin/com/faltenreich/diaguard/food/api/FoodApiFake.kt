package com.faltenreich.diaguard.food.api

import com.faltenreich.diaguard.shared.data.PagingPage

class FoodApiFake : FoodApi {

    override suspend fun search(query: String?, page: PagingPage): List<FoodFromApi> {
        TODO("Not yet implemented")
    }
}