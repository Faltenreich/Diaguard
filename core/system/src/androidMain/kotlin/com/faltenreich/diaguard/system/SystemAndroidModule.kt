package com.faltenreich.diaguard.system

import com.faltenreich.diaguard.config.BuildConfig
import com.faltenreich.diaguard.system.notification.AlarmManager
import com.faltenreich.diaguard.system.notification.AndroidAlarmManager
import com.faltenreich.diaguard.system.notification.AndroidNotificationManager
import com.faltenreich.diaguard.system.notification.FakeAlarmManager
import com.faltenreich.diaguard.system.permission.AndroidPermissionManager
import com.faltenreich.diaguard.system.permission.PermissionManager
import com.faltenreich.diaguard.system.settings.AndroidSystemSettings
import com.faltenreich.diaguard.system.settings.SystemSettings
import com.faltenreich.diaguard.system.web.AndroidUrlOpener
import com.faltenreich.diaguard.system.web.UrlOpener
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun systemPlatformModule() = module {
    singleOf(::AndroidPermissionManager) bind PermissionManager::class

    factory<AlarmManager> {
        if (get<BuildConfig>().hasPlatformFramework()) AndroidAlarmManager(androidContext())
        else FakeAlarmManager()
    }
    factoryOf(::AndroidNotificationManager)

    factory<SystemSettings> {
        if (get<BuildConfig>().hasPlatformFramework()) AndroidSystemSettings(context = get())
        else object : SystemSettings {
            override fun openNotificationSettings() = Unit
        }
    }

    factory<UrlOpener> {
        if (get<BuildConfig>().hasPlatformFramework()) AndroidUrlOpener(context = get())
        else UrlOpener {}
    }
}