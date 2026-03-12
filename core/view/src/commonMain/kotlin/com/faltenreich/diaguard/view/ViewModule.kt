package com.faltenreich.diaguard.view

import org.koin.core.module.Module
import org.koin.dsl.module

fun viewModule() = module {
    includes(viewPlatformModule())
}

internal expect fun viewPlatformModule(): Module