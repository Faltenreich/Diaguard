package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.IsFirstVisitUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    isFirstVisit: IsFirstVisitUseCase = inject(),
) : ViewModel() {

    private val state: Flow<DashboardViewState> = isFirstVisit().map { isFirstVisit ->
        when (isFirstVisit) {
            true -> DashboardViewState.FirstVisit
            false -> DashboardViewState.Revisit()
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DashboardViewState.Loading,
    )
}