package com.faltenreich.diaguard.navigation

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun navigationModule() = module {

    factoryOf(::NavigateToUseCase)
    factoryOf(::NavigateBackUseCase)
    factoryOf(::ShowSnackbarUseCase)
}