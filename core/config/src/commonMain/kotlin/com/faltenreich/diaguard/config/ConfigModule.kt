package com.faltenreich.diaguard.config

import org.koin.dsl.module

fun configModule() = module {
    includes(buildConfigModule())
}