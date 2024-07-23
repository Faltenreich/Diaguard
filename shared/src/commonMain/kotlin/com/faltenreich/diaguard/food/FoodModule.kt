package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsMapper
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForFoodUseCase
import com.faltenreich.diaguard.food.form.DeleteFoodUseCase
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.food.form.StoreFoodUseCase
import com.faltenreich.diaguard.food.form.ValidateFoodInputUseCase
import com.faltenreich.diaguard.food.search.SearchFoodUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun foodModule() = module {
    singleOf(::OpenFoodFactsMapper)

    single<FoodApi> { OpenFoodFactsApi() }
    singleOf(::FoodRepository)
    singleOf(::FoodEatenRepository)

    singleOf(::ValidateFoodInputUseCase)
    singleOf(::StoreFoodUseCase)
    singleOf(::DeleteFoodUseCase)
    singleOf(::SearchFoodUseCase)
    singleOf(::GetFoodEatenForFoodUseCase)
    singleOf(::GetFoodEatenForEntryUseCase)
    singleOf(::StoreFoodEatenUseCase)

    factory { (food: Food.Local?) -> FoodFormViewModel(food) }
    factory { (food: Food.Local) -> FoodEatenListViewModel(food) }
}