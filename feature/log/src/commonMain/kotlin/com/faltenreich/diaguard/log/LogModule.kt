package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.data.dataModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun logModule() = module {
    includes(dataModule())

    factoryOf(::InvalidateLogDayStickyInfoUseCase)

    viewModelOf(::LogViewModel)
}