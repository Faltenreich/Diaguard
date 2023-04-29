package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.CoroutineScope

actual abstract class ViewModel {

    actual val viewModelScope: CoroutineScope = TODO()

    protected actual open fun onCleared() {
        TODO()
    }
}