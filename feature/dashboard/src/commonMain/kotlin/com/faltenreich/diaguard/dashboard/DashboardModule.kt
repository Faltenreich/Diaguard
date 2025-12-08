package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetDashboardAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cEstimatedUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cLatestUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetDashboardLatestUseCase
import com.faltenreich.diaguard.dashboard.reminder.GetDashboardReminderUseCase
import com.faltenreich.diaguard.dashboard.today.GetDashboardTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetDashboardTrendUseCase
import com.faltenreich.diaguard.data.dataModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun dashboardModule() = module {
    includes(dataModule())

    factoryOf(::GetDashboardLatestUseCase)
    factoryOf(::GetDashboardReminderUseCase)
    factoryOf(::GetDashboardTodayUseCase)
    factoryOf(::GetDashboardAverageUseCase)
    factoryOf(::GetDashboardHbA1cEstimatedUseCase)
    factoryOf(::GetDashboardHbA1cLatestUseCase)
    factoryOf(::GetDashboardHbA1cUseCase)
    factoryOf(::GetDashboardTrendUseCase)

    viewModelOf(::DashboardViewModel)
}