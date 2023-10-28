package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.list.FoodListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun foodModule() = module {
    singleOf(::FoodRepository)

    singleOf(::FoodListViewModel)
}