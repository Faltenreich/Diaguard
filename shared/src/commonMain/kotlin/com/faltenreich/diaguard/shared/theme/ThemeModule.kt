package com.faltenreich.diaguard.shared.theme

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun themeModule() = module {
    singleOf(::ThemeViewModel)
}