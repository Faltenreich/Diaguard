package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.log.item.InvalidateLogDayStickyHeaderInfoUseCase
import com.faltenreich.diaguard.shared.datetime.Date
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun logModule() = module {
    singleOf(::InvalidateLogDayStickyHeaderInfoUseCase)

    factory { (date: Date?) -> LogViewModel(date = date) }
}