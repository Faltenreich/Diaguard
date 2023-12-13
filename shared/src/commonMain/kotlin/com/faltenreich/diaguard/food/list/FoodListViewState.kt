package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.food.Food

sealed interface FoodListViewState {

    class Result(val items: List<Food>?): FoodListViewState
}