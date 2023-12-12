package com.faltenreich.diaguard.shared.architecture

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope

interface ViewModel : ScreenModel {

    val scope: CoroutineScope
        get() = screenModelScope
}