package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.CloseBottomSheetUseCase
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.navigation.system.OpenNotificationSettingsUseCase
import com.faltenreich.diaguard.navigation.system.OpenUrlUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::Navigation)

    singleOf(::CollectNavigationEventsUseCase)
    singleOf(::GetTopAppBarStyleUseCase)
    singleOf(::GetBottomAppBarStyleUseCase)
    singleOf(::PushScreenUseCase)
    singleOf(::PopScreenUseCase)
    singleOf(::OpenNotificationSettingsUseCase)
    singleOf(::OpenUrlUseCase)

    singleOf(::OpenBottomSheetUseCase)
    singleOf(::CloseBottomSheetUseCase)

    singleOf(::ShowSnackbarUseCase)
}