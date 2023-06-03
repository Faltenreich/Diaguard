package com.faltenreich.diaguard.log

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::LogViewModel)
}