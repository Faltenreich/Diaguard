package com.faltenreich.diaguard.shared.system

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun systemSettingsModule() = module {
    factory<SystemSettings> { AndroidSystemSettings(androidContext()) }
}