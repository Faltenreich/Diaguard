package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyInfoUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::InvalidateLogDayStickyInfoUseCase)

    viewModelOf(::LogViewModel)
}