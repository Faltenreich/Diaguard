package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetAverageBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetCurrentHbA1cUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetEstimatedHbA1cUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetLatestHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.today.GetTodayUseCase
import com.faltenreich.diaguard.dashboard.trend.GetTrendUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun dashboardModule() = module {
    factoryOf(::GetLatestBloodSugarUseCase)
    factoryOf(::GetTodayUseCase)
    factoryOf(::GetAverageBloodSugarUseCase)
    factoryOf(::GetEstimatedHbA1cUseCase)
    factoryOf(::GetLatestHbA1cUseCase)
    factoryOf(::GetCurrentHbA1cUseCase)
    factoryOf(::GetTrendUseCase)

    viewModelOf(::DashboardViewModel)
}