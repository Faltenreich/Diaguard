package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetTodayUseCase
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
    getLatestBloodSugar: GetLatestBloodSugarUseCase = inject(),
    getToday: GetTodayUseCase = inject(),
    getAverage: GetAverageUseCase = inject(),
) : ViewModel {

    private val state: Flow<DashboardViewState> = combine(
        getLatestBloodSugar(),
        getToday(),
        getAverage(),
        DashboardViewState::Revisit,
    ).flowOn(dispatcher)
    val viewState = state.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = DashboardViewState.Loading,
    )
}