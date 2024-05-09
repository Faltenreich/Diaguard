package com.faltenreich.diaguard.main

import com.faltenreich.diaguard.backup.HasDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun mainModule() = module {
    singleOf(::HasDataUseCase)

    factoryOf(::MainViewModel)
}