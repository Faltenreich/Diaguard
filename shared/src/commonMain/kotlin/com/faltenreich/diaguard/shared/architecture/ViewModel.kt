package com.faltenreich.diaguard.shared.architecture

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel : ScreenModel {

    val viewModelScope: CoroutineScope
        get() = coroutineScope
}