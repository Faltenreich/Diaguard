package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.average.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.hba1c.GetCurrentHbA1cUseCase
import com.faltenreich.diaguard.dashboard.latest.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.today.GetTodayUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun dashboardModule() = module {
    factoryOf(::GetLatestBloodSugarUseCase)
    factoryOf(::GetTodayUseCase)
    factoryOf(::GetAverageUseCase)
    factoryOf(::GetCurrentHbA1cUseCase)

    viewModelOf(::DashboardViewModel)
}