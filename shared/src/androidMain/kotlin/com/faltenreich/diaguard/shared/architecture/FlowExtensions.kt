package com.faltenreich.diaguard.shared.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(context: CoroutineContext): State<T> {
    return collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
        minActiveState = Lifecycle.State.STARTED,
        context,
    )
}