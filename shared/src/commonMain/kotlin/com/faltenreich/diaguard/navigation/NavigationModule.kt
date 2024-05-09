package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.main.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::Navigation)

    singleOf(::NavigateBackUseCase)
    singleOf(::CanNavigateBackUseCase)
    singleOf(::NavigateToScreenUseCase)
    singleOf(::ShowSnackbarUseCase)
    singleOf(::GetActiveScreenUseCase)
    singleOf(::GetModalUseCase)
    singleOf(::OpenModalUseCase)
    singleOf(::CloseModalUseCase)

    factoryOf(::MainViewModel)
}