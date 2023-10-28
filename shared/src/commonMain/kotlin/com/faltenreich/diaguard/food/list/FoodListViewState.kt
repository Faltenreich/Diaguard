package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed class FoodListViewState(val query: String?) {

    class Loading(query: String?): FoodListViewState(query)

    class Result(query: String?, val items: List<Food>): FoodListViewState(query)
}