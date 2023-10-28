package com.faltenreich.diaguard.food.list

import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FoodListViewModel(
    private val dispatcher: CoroutineDispatcher = inject(),
    private val searchFood: SearchFoodUseCase = inject(),
) : ViewModel() {

    private val state = MutableStateFlow<FoodListViewState>(FoodListViewState.Loading(query = null))
    val viewState = state.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) {
            state
                .filterIsInstance<FoodListViewState.Loading>()
                .debounce(1.seconds)
                .distinctUntilChanged()
                .flatMapLatest { state -> searchFood(query = state.query) }
                .onEach { results -> state.value = FoodListViewState.Result(state.value.query, results) }
                .collect()
        }
    }

    fun onQueryChange(query: String) = viewModelScope.launch(dispatcher) {
        state.value = FoodListViewState.Loading(query = query.takeIf(String::isNotBlank))
    }
}