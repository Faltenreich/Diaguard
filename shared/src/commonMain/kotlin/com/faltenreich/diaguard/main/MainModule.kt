package com.faltenreich.diaguard.main

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun mainModule() = module {
    factoryOf(::GetNavigationEventUseCase)
    viewModelOf(::MainViewModel)
}