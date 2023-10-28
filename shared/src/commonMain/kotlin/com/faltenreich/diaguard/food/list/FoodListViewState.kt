package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed class FoodListViewState(
    val query: String?,
    val items: List<Food>?,
) {

    class Loading(query: String?): FoodListViewState(query, null)

    class Result(
        query: String?,
        items: List<Food>,
    ): FoodListViewState(query, items)
}