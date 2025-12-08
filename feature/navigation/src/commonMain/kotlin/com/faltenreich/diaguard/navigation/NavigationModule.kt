package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.data.dataModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun navigationModule() = module {
    includes(dataModule())

    factoryOf(::NavigateToUseCase)
    factoryOf(::NavigateBackUseCase)
    factoryOf(::ShowSnackbarUseCase)
}