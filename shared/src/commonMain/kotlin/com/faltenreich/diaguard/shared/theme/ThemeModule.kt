package com.faltenreich.diaguard.shared.theme

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun themeModule() = module {
    factoryOf(::ThemeViewModel)
}