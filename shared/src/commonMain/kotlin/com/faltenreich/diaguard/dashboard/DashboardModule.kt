package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetDashboardAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cCurrentUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cEstimatedUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetDashboardHbA1cLatestUseCase
import com.faltenreich.diaguard.dashboard.latest.GetDashboardLatestUseCase
import com.faltenreich.diaguard.dashboard.today.GetDashboardTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetDashboardTrendUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun dashboardModule() = module {
    factoryOf(::GetDashboardLatestUseCase)
    factoryOf(::GetDashboardTodayUseCase)
    factoryOf(::GetDashboardAverageUseCase)
    factoryOf(::GetDashboardHbA1cEstimatedUseCase)
    factoryOf(::GetDashboardHbA1cLatestUseCase)
    factoryOf(::GetDashboardHbA1cCurrentUseCase)
    factoryOf(::GetDashboardTrendUseCase)

    viewModelOf(::DashboardViewModel)
}