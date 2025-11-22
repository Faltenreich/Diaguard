package com.faltenreich.diaguard.config

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun configPlatformModule(): Module = module {
    factory<BuildConfig> { TODO("Not yet implemented") }
}