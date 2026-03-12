package com.faltenreich.diaguard.system

import com.faltenreich.diaguard.system.permission.HasPermissionUseCase
import com.faltenreich.diaguard.system.permission.RequestPermissionUseCase
import com.faltenreich.diaguard.system.settings.OpenNotificationSettingsUseCase
import com.faltenreich.diaguard.system.web.OpenUrlUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun systemModule() = module {
    includes(systemPlatformModule())

    factoryOf(::HasPermissionUseCase)
    factoryOf(::RequestPermissionUseCase)

    factoryOf(::OpenNotificationSettingsUseCase)

    factoryOf(::OpenUrlUseCase)
}

internal expect fun systemPlatformModule(): Module