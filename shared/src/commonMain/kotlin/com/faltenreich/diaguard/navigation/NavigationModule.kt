package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.snack.ShowSnackbarUseCase
import com.faltenreich.diaguard.main.menu.MainMenuViewModel
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.GetBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.GetModalUseCase
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.screen.CanNavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.GetActiveScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetCurrentScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateBackUseCase
import com.faltenreich.diaguard.navigation.screen.NavigateToScreenUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun navigationModule() = module {
    single { Navigation(dispatcher = Dispatchers.Main) }

    singleOf(::GetCurrentScreenUseCase)
    singleOf(::GetActiveScreenUseCase)
    singleOf(::GetTopAppBarStyleUseCase)
    singleOf(::GetBottomAppBarStyleUseCase)
    singleOf(::NavigateToScreenUseCase)
    singleOf(::NavigateBackUseCase)
    singleOf(::CanNavigateBackUseCase)

    singleOf(::GetBottomSheetUseCase)
    singleOf(::OpenBottomSheetUseCase)
    singleOf(::CloseBottomSheetUseCase)

    singleOf(::GetModalUseCase)
    singleOf(::OpenModalUseCase)
    singleOf(::CloseModalUseCase)

    singleOf(::ShowSnackbarUseCase)

    viewModelOf<MainMenuViewModel>(::MainMenuViewModel)
}