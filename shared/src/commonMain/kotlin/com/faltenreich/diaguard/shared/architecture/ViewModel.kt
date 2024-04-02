package com.faltenreich.diaguard.shared.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class ViewModel<State, Intent>(
    private val dispatcher: CoroutineDispatcher = inject(),
) : ScreenModel {

    val scope: CoroutineScope
        get() = screenModelScope

    abstract val state: Flow<State>

    val stateInScope: StateFlow<State?> by lazy {
        state.flowOn(dispatcher).stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )
    }

    @Composable
    fun collectState(): State? {
        return stateInScope.collectAsState().value
    }

    abstract fun handleIntent(intent: Intent)

    fun dispatchIntent(intent: Intent) {
        scope.launch(dispatcher) { handleIntent(intent) }
    }
}