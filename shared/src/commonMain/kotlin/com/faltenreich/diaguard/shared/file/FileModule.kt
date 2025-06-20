package com.faltenreich.diaguard.shared.file

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun fileModule() = module {
    factoryOf(::SystemFileReader)
}