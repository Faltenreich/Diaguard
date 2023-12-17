package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Forms use MutableState instead of (State-)Flow,
 * since the latter is not compatible with Composables like [androidx.compose.material3.TextField]
 */
abstract class FormViewModel<Intent> : ViewModel<Unit, Intent>() {

    override val state: Flow<Unit>
        get() = flowOf(Unit)
}