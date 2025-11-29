package com.faltenreich.diaguard.log

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun logModule() = module {
    factoryOf(::InvalidateLogDayStickyInfoUseCase)

    viewModelOf(::LogViewModel)
}