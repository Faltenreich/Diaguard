package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bottom.GetBottomSheetUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    single { Navigation(dispatcher = Dispatchers.Main) }

    singleOf(::GetCurrentScreenUseCase)
    singleOf(::GetBottomSheetUseCase)
    singleOf(::NavigateBackUseCase)
    singleOf(::CanNavigateBackUseCase)
    singleOf(::NavigateToScreenUseCase)
    singleOf(::ShowSnackbarUseCase)
    singleOf(::GetActiveScreenUseCase)
    singleOf(::OpenBottomSheetUseCase)
    singleOf(::CloseBottomSheetUseCase)
    singleOf(::GetModalUseCase)
    singleOf(::OpenModalUseCase)
    singleOf(::CloseModalUseCase)
}