package com.faltenreich.diaguard.shared.primitive

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun primitiveModule() = module {
    factoryOf(::NumberFormatter)
}