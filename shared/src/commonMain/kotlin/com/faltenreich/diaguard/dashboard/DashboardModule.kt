package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetAverageUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.GetTodayUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dashboardModule() = module {
    singleOf(::GetLatestBloodSugarUseCase)
    singleOf(::GetTodayUseCase)
    singleOf(::GetAverageUseCase)

    singleOf(::DashboardViewModel)
}