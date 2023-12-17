package com.faltenreich.diaguard.food.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.seconds

class FoodListViewModel(
    private val searchFood: SearchFoodUseCase = inject(),
) : ViewModel<List<Food>?, Unit>() {

    override val state = MutableStateFlow<List<Food>?>(null)

    var query: String by mutableStateOf("")

    init {
        snapshotFlow { query }
            .debounce(1.seconds)
            .distinctUntilChanged()
            .onEach { state.value = null }
            .flatMapLatest { searchFood(it) }
            .onEach { state.value = it }
            .launchIn(scope)
    }

    override fun onIntent(intent: Unit) = Unit
}