package com.faltenreich.diaguard.shared.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class ViewModel<State> : ScreenModel {

    val scope: CoroutineScope
        get() = screenModelScope

    abstract val state: Flow<State>

    val stateInScope: StateFlow<State?> by lazy {
        state.stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )
    }

    @Composable
    fun collectState(): State? {
        return stateInScope.collectAsState().value
    }
}