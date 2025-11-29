package com.faltenreich.diaguard.shared.config

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun configModule(): Module = module {
    factoryOf(::AndroidBuildConfig) bind BuildConfig::class
}