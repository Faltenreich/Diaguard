package com.faltenreich.diaguard.config

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun configPlatformModule(): Module = module {
    factoryOf(::AndroidBuildConfig) bind BuildConfig::class
}