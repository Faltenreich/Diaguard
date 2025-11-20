package com.faltenreich.diaguard.persistence.file

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun fileModule() = module {
    factoryOf(::SystemFileReader)
}