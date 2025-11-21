package com.faltenreich.diaguard.system

import com.faltenreich.diaguard.system.permission.AndroidPermissionManager
import com.faltenreich.diaguard.system.permission.PermissionManager
import com.faltenreich.diaguard.system.settings.AndroidSystemSettings
import com.faltenreich.diaguard.system.settings.SystemSettings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun systemPlatformModule() = module {
    singleOf(::AndroidPermissionManager) bind PermissionManager::class
    factoryOf(::AndroidSystemSettings) bind SystemSettings::class
}