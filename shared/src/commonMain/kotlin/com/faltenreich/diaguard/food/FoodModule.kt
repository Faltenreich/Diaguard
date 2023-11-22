package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.detail.FoodDetailViewModel
import com.faltenreich.diaguard.food.detail.eaten.FoodEatenListViewModel
import com.faltenreich.diaguard.food.detail.eaten.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.food.detail.eaten.GetFoodEatenForFoodUseCase
import com.faltenreich.diaguard.food.detail.nutrient.FoodNutrientListViewModel
import com.faltenreich.diaguard.food.eaten.CreateFoodEatenUseCase
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.food.list.FoodListViewModel
import com.faltenreich.diaguard.food.list.SearchFoodUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun foodModule() = module {
    singleOf(::FoodRepository)
    singleOf(::FoodEatenRepository)

    singleOf(::SearchFoodUseCase)
    singleOf(::GetFoodEatenForFoodUseCase)
    singleOf(::GetFoodEatenForEntryUseCase)
    singleOf(::CreateFoodEatenUseCase)

    singleOf(::FoodListViewModel)
    factory { (food: Food) -> FoodDetailViewModel(food = food) }
    factory { (food: Food) -> FoodNutrientListViewModel(food = food) }
    factory { (food: Food) -> FoodEatenListViewModel(food = food) }
    factory { (food: Food) -> FoodFormViewModel(food = food) }
}