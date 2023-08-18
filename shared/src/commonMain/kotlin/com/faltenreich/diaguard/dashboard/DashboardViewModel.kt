package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.IsFirstVisitUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    isFirstVisit: IsFirstVisitUseCase = inject(),
    getLatestBloodSugar: GetLatestBloodSugarUseCase = inject(),
) : ViewModel() {

    private val state: Flow<DashboardViewState> = combine(
        isFirstVisit(),
        getLatestBloodSugar(),
    ) { isFirstVisit, latestBloodSugar ->
        when (isFirstVisit) {
            true -> DashboardViewState.FirstVisit
            false -> DashboardViewState.Revisit(
                bloodSugar = latestBloodSugar?.run {
                    DashboardViewState.Revisit.BloodSugar(
                        value = value.toString(),
                        dateTime = entry.dateTime.toString(),
                        ago = "",
                    )
                },
            )
        }
    }
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DashboardViewState.Loading,
    )
}