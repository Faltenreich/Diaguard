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
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

class FoodSearchViewModel(
    // FIXME: NoParameterFoundException: Can't get injected parameter #0 from DefinitionParameters[] for type 'com.faltenreich.diaguard.food.search.FoodSearchMode'
    private val mode: FoodSearchMode,
    private val searchFood: SearchFoodUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<FoodSearchState, FoodSearchIntent, FoodSearchEvent>() {

    var query: String by mutableStateOf("")

    private val pagingData: Flow<PagingData<Food.Local>> = snapshotFlow { query }
        .debounce(1.seconds)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            Pager(
                config = FoodSearchSource.newConfig(),
                pagingSourceFactory = { FoodSearchSource(query, searchFood) },
            ).flow
    }.cachedIn(scope)

    override val state = flowOf(FoodSearchState(pagingData = pagingData))

    override suspend fun handleIntent(intent: FoodSearchIntent) = with(intent) {
        when (this) {
            is FoodSearchIntent.Close -> navigateBack()
            is FoodSearchIntent.Create -> navigateToScreen(FoodFormScreen())
            is FoodSearchIntent.Select -> selectFood(food)
        }
    }

    private suspend fun selectFood(food: Food.Local) {
        when (mode) {
            FoodSearchMode.STROLL -> navigateToScreen(FoodFormScreen(food))
            FoodSearchMode.FIND -> {
                postEvent(FoodSearchEvent.Select(food))
                navigateBack()
            }
        }
    }
}