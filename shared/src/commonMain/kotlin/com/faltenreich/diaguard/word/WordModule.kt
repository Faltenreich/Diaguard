package com.faltenreich.diaguard.word

import org.koin.dsl.module

fun wordModule() = module {
    factory { parameters -> WordViewModel(parameters.get()) }
}