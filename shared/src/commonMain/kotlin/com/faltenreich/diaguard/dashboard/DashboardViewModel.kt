package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.today.GetTodayUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class DashboardViewModel(
    getLatestBloodSugar: GetLatestBloodSugarUseCase,
    getToday: GetTodayUseCase,
    getAverage: GetAverageUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
) : ViewModel<DashboardViewState, DashboardIntent, Unit>() {

    override val state: Flow<DashboardViewState> = combine(
        getLatestBloodSugar(),
        getToday(),
        getAverage(),
        ::DashboardViewState,
    )

    override fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is DashboardIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is DashboardIntent.EditEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}