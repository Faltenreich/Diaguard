package com.faltenreich.diaguard.food

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.entry.form.GetFoodByIdUseCase
import com.faltenreich.diaguard.food.eaten.list.FoodEatenListViewModel
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
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.preference.preferenceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun foodModule() = module {
    includes(
        dataModule(),
        navigationModule(),
        preferenceModule(),
    )

    factoryOf(::GetFoodByIdUseCase)
    factoryOf(::CreateFoodFormInputUseCase)
    factoryOf(::ValidateFoodInputUseCase)
    factoryOf(::StoreFoodUseCase)
    factoryOf(::DeleteFoodUseCase)
    factoryOf(::SearchFoodUseCase)
    factoryOf(::GetFoodEatenForFoodUseCase)

    viewModel { (mode: FoodSearchMode) -> FoodSearchViewModel(mode) }
    viewModelOf(::FoodSelectionViewModel)
    viewModel { (foodId: Long?) -> FoodFormViewModel(foodId) }
    viewModel { (foodId: Long) -> FoodEatenListViewModel(foodId) }
}