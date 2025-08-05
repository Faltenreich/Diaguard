package com.faltenreich.diaguard.shared.notification

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun notificationModule() = module {
    factoryOf(::AndroidAlarmManager) bind AlarmManager::class
}