package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyHeaderInfoUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::InvalidateLogDayStickyHeaderInfoUseCase)
    singleOf(::LogViewModel)
}