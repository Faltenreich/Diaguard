package com.faltenreich.diaguard.shared.architecture

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {

    val viewModelScope: CoroutineScope

    protected open fun onCleared()
}