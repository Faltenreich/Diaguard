package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.architecture.viewmodel.ViewModel
import com.faltenreich.diaguard.dashboard.average.GetDashboardAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetDashboardLatestUseCase
import com.faltenreich.diaguard.dashboard.reminder.GetDashboardReminderUseCase
import com.faltenreich.diaguard.dashboard.today.GetDashboardTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetDashboardTrendUseCase
import com.faltenreich.diaguard.entry.form.reminder.SetReminderUseCase
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.screen.NavigateToUseCase
import kotlinx.coroutines.flow.combine

class DashboardViewModel(
    getLatest: GetDashboardLatestUseCase,
    getReminder: GetDashboardReminderUseCase,
    getToday: GetDashboardTodayUseCase,
    getAverage: GetDashboardAverageUseCase,
    getCurrentHbA1c: GetDashboardHbA1cUseCase,
    getTrend: GetDashboardTrendUseCase,
    private val navigateTo: NavigateToUseCase,
    private val setReminder: SetReminderUseCase,
) : ViewModel<DashboardState, DashboardIntent, Unit>() {

    override val state = combine(
        getLatest(),
        getReminder(),
        getToday(),
        getAverage(),
        getCurrentHbA1c(),
        getTrend(),
        ::DashboardState,
    )

    override suspend fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.CreateEntry -> navigateTo(NavigationTarget.EntryForm())
            is DashboardIntent.EditEntry -> navigateTo(NavigationTarget.EntryForm(entryId = intent.entry.id))
            is DashboardIntent.OpenStatistic -> navigateTo(NavigationTarget.Statistic)
            is DashboardIntent.SearchEntries -> navigateTo(NavigationTarget.EntrySearch())
            is DashboardIntent.DeleteReminder -> setReminder(null)
        }
    }
}