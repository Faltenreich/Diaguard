package com.faltenreich.diaguard.startup.seed

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun seedModule() = module {
    factoryOf(::ImportSeedUseCase)
}