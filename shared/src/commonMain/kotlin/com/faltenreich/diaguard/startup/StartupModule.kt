package com.faltenreich.diaguard.startup

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun startupModule() = module {
    factoryOf(::HasDataUseCase)
    factoryOf(::MigrateDataUseCase)
}