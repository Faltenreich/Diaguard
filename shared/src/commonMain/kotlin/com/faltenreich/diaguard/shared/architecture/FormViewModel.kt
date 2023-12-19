package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.flow.Flow

/**
 * Forms use MutableState instead of (State-)Flow,
 * since the latter is not compatible with Composables like [androidx.compose.material3.TextField]
 */
abstract class FormViewModel<Intent> : ViewModel<Nothing, Intent>() {

    override val state: Flow<Nothing>
        get() = throw UnsupportedOperationException()
}