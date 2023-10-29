package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed class FoodListViewState(val items: List<Food>?) {

    data object Loading: FoodListViewState(null)

    class Result(items: List<Food>): FoodListViewState(items)
}