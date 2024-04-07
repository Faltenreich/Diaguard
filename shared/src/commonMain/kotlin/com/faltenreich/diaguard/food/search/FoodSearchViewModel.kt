package com.faltenreich.diaguard.food.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.navigation.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.FoodFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FoodSearchViewModel(
    private val mode: FoodSearchMode,
    private val searchFood: SearchFoodUseCase = inject(),
    private val navigateBack: NavigateBackUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<FoodSearchState, FoodSearchIntent, FoodSearchEvent>() {

    override val state = MutableStateFlow<FoodSearchState>(FoodSearchState.Loading)

    var query: String by mutableStateOf("")

    init {
        snapshotFlow { query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { state.value = FoodSearchState.Loading }
            .flatMapLatest(searchFood::invoke)
            .onEach { results ->
                state.value =
                    if (results.isNotEmpty()) FoodSearchState.Loaded(results)
                    else FoodSearchState.Empty
            }
            .launchIn(scope)
    }

    override fun handleIntent(intent: FoodSearchIntent) {
        when (intent) {
            is FoodSearchIntent.Close -> navigateBack()
            is FoodSearchIntent.Create -> navigateToScreen(FoodFormScreen())
            is FoodSearchIntent.Select -> selectFood(intent.food)
        }
    }

    private fun selectFood(food: Food) = scope.launch {
        when (mode) {
            FoodSearchMode.STROLL -> navigateToScreen(FoodFormScreen(food))
            FoodSearchMode.FIND -> {
                postEvent(FoodSearchEvent.Select(food))
                navigateBack()
            }
        }
    }
}