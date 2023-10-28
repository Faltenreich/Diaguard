package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.list.FoodListViewModel
import com.faltenreich.diaguard.food.list.SearchFoodUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun foodModule() = module {
    singleOf(::FoodRepository)

    singleOf(::SearchFoodUseCase)

    singleOf(::FoodListViewModel)
}