package com.faltenreich.diaguard.system

import com.faltenreich.diaguard.system.notification.AlarmManager
import com.faltenreich.diaguard.system.notification.AndroidAlarmManager
import com.faltenreich.diaguard.system.notification.AndroidNotificationManager
import com.faltenreich.diaguard.system.permission.AndroidPermissionManager
import com.faltenreich.diaguard.system.permission.PermissionManager
import com.faltenreich.diaguard.system.settings.AndroidSystemSettings
import com.faltenreich.diaguard.system.settings.SystemSettings
import com.faltenreich.diaguard.system.web.AndroidUrlOpener
import com.faltenreich.diaguard.system.web.UrlOpener
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun systemPlatformModule() = module {
    singleOf(::AndroidPermissionManager) bind PermissionManager::class

    factoryOf(::AndroidAlarmManager) bind AlarmManager::class
    factoryOf(::AndroidNotificationManager)

    factoryOf(::AndroidSystemSettings) bind SystemSettings::class

    factoryOf(::AndroidUrlOpener) bind UrlOpener::class
}