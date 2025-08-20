package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bar.snackbar.ShowSnackbarUseCase
import com.faltenreich.diaguard.navigation.screen.GetBottomAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.GetTopAppBarStyleUseCase
import com.faltenreich.diaguard.navigation.screen.PopScreenUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.navigation.system.OpenNotificationSettingsUseCase
import com.faltenreich.diaguard.navigation.system.OpenPermissionSettingsUseCase
import com.faltenreich.diaguard.navigation.system.OpenUrlUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun navigationModule() = module {
    singleOf(::Navigation)

    factoryOf(::CollectNavigationEventsUseCase)
    factoryOf(::GetTopAppBarStyleUseCase)
    factoryOf(::GetBottomAppBarStyleUseCase)

    factoryOf(::PushScreenUseCase)
    factoryOf(::PopScreenUseCase)

    factoryOf(::OpenNotificationSettingsUseCase)
    factoryOf(::OpenPermissionSettingsUseCase)
    factoryOf(::OpenUrlUseCase)
    factoryOf(::ShowSnackbarUseCase)
}