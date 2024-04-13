package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.api.OpenFoodFactsApi
import com.faltenreich.diaguard.food.eaten.CreateFoodEatenUseCase
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForFoodUseCase
import com.faltenreich.diaguard.food.form.CreateFoodUseCase
import com.faltenreich.diaguard.food.form.DeleteFoodUseCase
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchViewModel
import com.faltenreich.diaguard.food.search.SearchFoodUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun foodModule() = module {
    singleOf(::OpenFoodFactsApi)
    singleOf(::FoodRepository)
    singleOf(::FoodEatenRepository)

    singleOf(::CreateFoodUseCase)
    singleOf(::DeleteFoodUseCase)
    singleOf(::SearchFoodUseCase)
    singleOf(::GetFoodEatenForFoodUseCase)
    singleOf(::GetFoodEatenForEntryUseCase)
    singleOf(::CreateFoodEatenUseCase)

    factory { (mode: FoodSearchMode) -> FoodSearchViewModel(mode) }
    factory { (food: Food) -> FoodEatenListViewModel(food) }
    factory { (food: Food) -> FoodFormViewModel(food) }
}