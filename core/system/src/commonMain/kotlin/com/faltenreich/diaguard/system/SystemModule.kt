package com.faltenreich.diaguard.system

import com.faltenreich.diaguard.system.permission.HasPermissionUseCase
import com.faltenreich.diaguard.system.permission.RequestPermissionUseCase
import com.faltenreich.diaguard.system.settings.OpenNotificationSettingsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun systemModule() = module {
    includes(systemPlatformModule())

    factoryOf(::HasPermissionUseCase)
    factoryOf(::RequestPermissionUseCase)

    factoryOf(::OpenNotificationSettingsUseCase)
}

internal expect fun systemPlatformModule(): Module