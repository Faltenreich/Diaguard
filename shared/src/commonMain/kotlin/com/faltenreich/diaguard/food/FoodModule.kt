package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsMapper
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.food.eaten.StoreFoodEatenUseCase
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForEntryUseCase
import com.faltenreich.diaguard.food.eaten.list.GetFoodEatenForFoodUseCase
import com.faltenreich.diaguard.food.form.CreateFoodFormInputUseCase
import com.faltenreich.diaguard.food.form.DeleteFoodUseCase
import com.faltenreich.diaguard.food.form.FoodFormViewModel
import com.faltenreich.diaguard.food.form.StoreFoodUseCase
import com.faltenreich.diaguard.food.form.ValidateFoodInputUseCase
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchViewModel
import com.faltenreich.diaguard.food.search.FoodSelectionViewModel
import com.faltenreich.diaguard.food.search.SearchFoodUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun foodModule() = module {
    factoryOf(::OpenFoodFactsMapper)

    factoryOf(::OpenFoodFactsApi) bind FoodApi::class
    factoryOf(::FoodRepository)
    factoryOf(::FoodEatenRepository)

    factoryOf(::CreateFoodFormInputUseCase)
    factoryOf(::ValidateFoodInputUseCase)
    factoryOf(::StoreFoodUseCase)
    factoryOf(::DeleteFoodUseCase)
    factoryOf(::SearchFoodUseCase)
    factoryOf(::GetFoodEatenForFoodUseCase)
    factoryOf(::GetFoodEatenForEntryUseCase)
    factoryOf(::StoreFoodEatenUseCase)

    viewModel { (mode: FoodSearchMode) -> FoodSearchViewModel(mode) }
    viewModelOf(::FoodSelectionViewModel)
    viewModel { (foodId: Long?) -> FoodFormViewModel(foodId) }
    viewModel { (foodId: Long) -> FoodEatenListViewModel(foodId) }
}