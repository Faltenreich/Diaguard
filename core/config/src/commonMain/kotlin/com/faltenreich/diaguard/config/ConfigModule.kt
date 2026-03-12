package com.faltenreich.diaguard.config

import org.koin.core.module.Module
import org.koin.dsl.module

fun configModule() = module {
    includes(configPlatformModule())
}

internal expect fun configPlatformModule(): Module