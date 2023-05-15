package com.faltenreich.diaguard.dashboard

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun dashboardModule() = module {
    singleOf(::DashboardViewModel)
}