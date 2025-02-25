package com.faltenreich.diaguard.shared.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(context: CoroutineContext): State<T> {
    TODO("Not yet implemented")
}