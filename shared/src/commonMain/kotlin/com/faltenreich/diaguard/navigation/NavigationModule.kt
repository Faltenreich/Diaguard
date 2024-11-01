package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.main.menu.MainMenuViewModel
import com.faltenreich.diaguard.navigation.bar.snack.AndroidxSnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.snack.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.GetBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.StateFlowBottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.CloseModalUseCase
import com.faltenreich.diaguard.navigation.modal.GetModalUseCase
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.modal.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.StateFlowModalNavigation
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetCurrentScreenUseCase
import com.faltenreich.diaguard.navigation.screen.GetLatestScreenResultUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf<BottomSheetNavigation>(::StateFlowBottomSheetNavigation)
    singleOf<ModalNavigation>(::StateFlowModalNavigation)
    singleOf<SnackbarNavigation>(::AndroidxSnackbarNavigation)
    singleOf(::Navigation)

    singleOf(::GetCurrentScreenUseCase)
    singleOf(::GetTopAppBarStyleUseCase)
    singleOf(::GetBottomAppBarStyleUseCase)
    singleOf(::PushScreenUseCase)
    singleOf(::PopScreenUseCase)
    singleOf(::GetLatestScreenResultUseCase)

    singleOf(::GetBottomSheetUseCase)
    singleOf(::OpenBottomSheetUseCase)
    singleOf(::CloseBottomSheetUseCase)

    singleOf(::GetModalUseCase)
    singleOf(::OpenModalUseCase)
    singleOf(::CloseModalUseCase)

    singleOf(::ShowSnackbarUseCase)

    viewModelOf<MainMenuViewModel>(::MainMenuViewModel)
}