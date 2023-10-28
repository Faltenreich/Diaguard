package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed class FoodListViewState(val query: String) {

    data object Idle: FoodListViewState("")

    class Loading(query: String): FoodListViewState(query)

    class Error(query: String): FoodListViewState(query)

    class Result(query: String, val results: List<Food>): FoodListViewState(query)
}