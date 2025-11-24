package com.faltenreich.diaguard.food.search

import androidx.paging.Pager
import androidx.paging.cachedIn
import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.data.food.search.FoodSearchParams
import com.faltenreich.diaguard.food.form.FoodFormScreen
import com.faltenreich.diaguard.injection.inject
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.food.ShowBrandedFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCommonFoodPreference
import com.faltenreich.diaguard.preference.food.ShowCustomFoodPreference
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FoodSearchViewModel(
    val mode: FoodSearchMode,
    getPreference: GetPreferenceUseCase = inject(),
    private val searchFood: SearchFoodUseCase = inject(),
    private val pushScreen: com.faltenreich.diaguard.navigation.screen.PushScreenUseCase = inject(),
    private val popScreen: com.faltenreich.diaguard.navigation.screen.PopScreenUseCase = inject(),
) : ViewModel<FoodSearchState, FoodSearchIntent, Unit>() {

    private val _query = MutableStateFlow("")
    private val query = MutableStateFlow(_query.value)

    override val state = combine(
        query,
        getPreference(ShowCommonFoodPreference),
        getPreference(ShowCustomFoodPreference),
        getPreference(ShowBrandedFoodPreference),
        ::FoodSearchParams,
    ).mapLatest { params ->
        FoodSearchState(
            query = params.query,
            pagingData = Pager(
                config = FoodSearchPagingSource.newConfig(),
                pagingSourceFactory = { FoodSearchPagingSource(params, searchFood) },
            ).flow.cachedIn(scope),
        )
    }

    init {
        scope.launch {
            _query
                .debounce(1.seconds)
                .distinctUntilChanged()
                .collect { update ->
                    query.update { update }
                }
        }
    }

    override suspend fun handleIntent(intent: FoodSearchIntent) {
        when (intent) {
            is FoodSearchIntent.SetQuery -> _query.update { intent.query }
            is FoodSearchIntent.Close -> popScreen()
            is FoodSearchIntent.Create -> pushScreen(FoodFormScreen())
            is FoodSearchIntent.OpenFood -> pushScreen(FoodFormScreen(intent.food))
            is FoodSearchIntent.OpenPreferences -> pushScreen(FoodPreferenceListScreen)
        }
    }
}