package com.faltenreich.diaguard.shared.clipboard

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun clipboardModule() = module {
    factoryOf(::Clipboard)
}