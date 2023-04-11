package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val screen = MutableStateFlow<Screen>(Screen.Dashboard)
    val uiState = screen.asStateFlow()

    fun navigate(screen: Screen) {
        this.screen.value = screen
    }
}