package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.DashboardAverageState
import com.faltenreich.diaguard.dashboard.hba1c.DashboardHbA1cState
import com.faltenreich.diaguard.dashboard.latest.DashboardLatestState
import com.faltenreich.diaguard.dashboard.reminder.DashboardReminderState
import com.faltenreich.diaguard.dashboard.today.DashboardTodayState
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState

data class DashboardState(
    val latest: DashboardLatestState?,
    val reminder: DashboardReminderState?,
    val today: DashboardTodayState,
    val average: DashboardAverageState,
    val hbA1c: DashboardHbA1cState,
    val trend: StatisticTrendState,
)