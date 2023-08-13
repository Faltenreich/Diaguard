package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val state = MutableStateFlow<DashboardViewState>(DashboardViewState.Loading)
    val viewState = state.asStateFlow()
}