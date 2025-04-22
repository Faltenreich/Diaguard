package com.faltenreich.diaguard.shared.theme

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun themeModule() = module {
    viewModelOf(::ThemeViewModel)
}