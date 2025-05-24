package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetDashboardAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetDashboardLatestUseCase
import com.faltenreich.diaguard.dashboard.today.GetDashboardTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetDashboardTrendUseCase
import com.faltenreich.diaguard.entry.form.EntryFormScreen
import com.faltenreich.diaguard.entry.search.EntrySearchScreen
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.statistic.StatisticScreen
import kotlinx.coroutines.flow.combine

class DashboardViewModel(
    getLatest: GetDashboardLatestUseCase,
    getToday: GetDashboardTodayUseCase,
    getAverage: GetDashboardAverageUseCase,
    getCurrentHbA1c: GetDashboardHbA1cUseCase,
    getTrend: GetDashboardTrendUseCase,
    private val pushScreen: PushScreenUseCase,
) : ViewModel<DashboardState, DashboardIntent, Unit>() {

    override val state = combine(
        getLatest(),
        getToday(),
        getAverage(),
        getCurrentHbA1c(),
        getTrend(),
        ::DashboardState,
    )

    override suspend fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> pushScreen(EntryFormScreen())
            is DashboardIntent.EditEntry -> pushScreen(EntryFormScreen(entry = intent.entry))
            is DashboardIntent.OpenStatistic -> pushScreen(StatisticScreen)
            is DashboardIntent.SearchEntries -> pushScreen(EntrySearchScreen())
        }
    }
}