package com.faltenreich.diaguard.food.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import com.faltenreich.diaguard.preference.FoodPreference
import com.faltenreich.diaguard.preference.food.FoodPreferenceScreen
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

class FoodSearchViewModel(
    private val mode: FoodSearchMode,
    getPreference: GetPreferenceUseCase = inject(),
    private val searchFood: SearchFoodUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<FoodSearchState, FoodSearchIntent, Unit>() {

    var query: String by mutableStateOf("")

    private val pagingData: Flow<PagingData<Food.Local>> = combine(
        snapshotFlow { query }
            .debounce(1.seconds)
            .distinctUntilChanged(),
        getPreference(FoodPreference.ShowCommonFood),
        getPreference(FoodPreference.ShowCustomFood),
        getPreference(FoodPreference.ShowBrandedFood),
        ::FoodSearchParams,
    ).flatMapLatest { params ->
        Pager(
            config = FoodSearchSource.newConfig(),
            pagingSourceFactory = { FoodSearchSource(params, searchFood) },
        ).flow
    }.cachedIn(scope)

    override val state = flowOf(FoodSearchState(pagingData = pagingData))

    override suspend fun handleIntent(intent: FoodSearchIntent) = with(intent) {
        when (this) {
            is FoodSearchIntent.Close -> navigateBack()
            is FoodSearchIntent.Create -> navigateToScreen(FoodFormScreen())
            is FoodSearchIntent.Select -> selectFood(food)
            is FoodSearchIntent.OpenPreferences -> navigateToScreen(FoodPreferenceScreen)
        }
    }

    private suspend fun selectFood(food: Food.Local) {
        when (mode) {
            FoodSearchMode.STROLL -> navigateToScreen(FoodFormScreen(food))
            FoodSearchMode.FIND -> navigateBack(result = KEY_SELECTED_FOOD_ID to food.id)
        }
    }

    companion object {

        const val KEY_SELECTED_FOOD_ID = "selectedFoodId"
    }
}