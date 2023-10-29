package com.faltenreich.diaguard.food.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class FoodListViewModel(
    private val searchFood: SearchFoodUseCase = inject(),
) : ViewModel() {

    private val state = MutableStateFlow<FoodListViewState>(FoodListViewState.Loading)
    val viewState = state.asStateFlow()

    var query: String by mutableStateOf("")

    init {
        snapshotFlow { query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { state.value = FoodListViewState.Loading }
            .flatMapLatest { searchFood(it) }
            .onEach { state.value = FoodListViewState.Result(it) }
            .launchIn(viewModelScope)
    }
}