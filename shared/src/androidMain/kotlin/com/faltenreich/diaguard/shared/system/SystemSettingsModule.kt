package com.faltenreich.diaguard.shared.system

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun systemSettingsModule() = module {
    factoryOf(::AndroidSystemSettings) bind SystemSettings::class
}