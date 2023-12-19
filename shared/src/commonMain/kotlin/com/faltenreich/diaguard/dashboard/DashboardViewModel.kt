package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetTodayUseCase
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardViewModel(
    private val navigateTo: NavigateToUseCase = inject(),
    getLatestBloodSugar: GetLatestBloodSugarUseCase = inject(),
    getToday: GetTodayUseCase = inject(),
    getAverage: GetAverageUseCase = inject(),
) : ViewModel<DashboardViewState, DashboardIntent>() {

    override val state: Flow<DashboardViewState> = combine(
        getLatestBloodSugar(),
        getToday(),
        getAverage(),
        DashboardViewState::Revisit,
    )

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> navigateTo(EntryFormScreen())
            is DashboardIntent.SearchEntries -> navigateTo(EntrySearchScreen())
            is DashboardIntent.EditEntry -> navigateTo(EntryFormScreen(entry = intent.entry))
        }
    }
}