package com.faltenreich.diaguard.dashboard

import com.faltenreich.diaguard.dashboard.usecase.GetLatestBloodSugarUseCase
import com.faltenreich.diaguard.dashboard.usecase.IsFirstVisitUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dashboardModule() = module {
    singleOf(::DashboardViewModel)

    singleOf(::IsFirstVisitUseCase)
    singleOf(::GetLatestBloodSugarUseCase)
}