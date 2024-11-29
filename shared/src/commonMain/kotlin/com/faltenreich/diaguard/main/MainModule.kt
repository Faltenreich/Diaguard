package com.faltenreich.diaguard.main

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun mainModule() = module {
    singleOf(::HasDataUseCase)
    singleOf(::MigrateDataUseCase)

    singleOf(::MainViewModel)
}