package com.faltenreich.diaguard.navigation

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::Navigation)
    singleOf(::NavigateToUseCase)
    singleOf(::NavigationViewModel)
}