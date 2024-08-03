package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.today.GetTodayUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class DashboardViewModel(
    getLatestBloodSugar: GetLatestBloodSugarUseCase = inject(),
    getToday: GetTodayUseCase = inject(),
    getAverage: GetAverageUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
) : ViewModel<DashboardState, DashboardIntent, Unit>() {

    override val state: Flow<DashboardState> = combine(
        getLatestBloodSugar(),
        getToday(),
        getAverage(),
        // TODO: Add use cases
        flowOf(null),
        flowOf(null),
        ::DashboardState,
    )

    override suspend fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is DashboardIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is DashboardIntent.EditEntry -> navigateToScreen(EntryFormScreen(entry = intent.entry))
        }
    }
}