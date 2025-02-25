package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle

@Composable
actual fun rememberLifecycleState(): LifecycleState {
    val state by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    return state.toDomain()
}

private fun Lifecycle.State.toDomain(): LifecycleState {
    return when (this) {
        Lifecycle.State.DESTROYED -> LifecycleState.DESTROYED
        Lifecycle.State.INITIALIZED -> LifecycleState.INITIALIZED
        Lifecycle.State.CREATED -> LifecycleState.CREATED
        Lifecycle.State.STARTED -> LifecycleState.STARTED
        Lifecycle.State.RESUMED -> LifecycleState.RESUMED
    }
}