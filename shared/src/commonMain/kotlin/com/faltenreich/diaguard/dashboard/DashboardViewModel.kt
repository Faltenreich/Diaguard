package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetTodayUseCase
import com.faltenreich.diaguard.dashboard.usecase.IsFirstVisitUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    dispatcher: CoroutineDispatcher = inject(),
    isFirstVisit: IsFirstVisitUseCase = inject(),
    getLatestBloodSugar: GetLatestBloodSugarUseCase = inject(),
    getToday: GetTodayUseCase = inject(),
) : ViewModel() {

    private val state: Flow<DashboardViewState> = combine(
        isFirstVisit(),
        getLatestBloodSugar(),
        getToday(),
    ) { isFirstVisit, latestBloodSugar, today ->
        when (isFirstVisit) {
            true -> DashboardViewState.FirstVisit
            false -> DashboardViewState.Revisit(
                latestBloodSugar = latestBloodSugar,
                today = today,
            )
        }
    }.flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DashboardViewState.Loading,
    )
}