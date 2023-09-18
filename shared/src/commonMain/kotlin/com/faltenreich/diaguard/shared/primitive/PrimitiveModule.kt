package com.faltenreich.diaguard.shared.primitive

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun primitiveModule() = module {
    singleOf(::NumberFormatter)
}