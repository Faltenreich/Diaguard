package com.faltenreich.diaguard.log

import com.faltenreich.diaguard.shared.datetime.Date
import org.koin.dsl.module

fun logModule() = module {
    factory { (date: Date?) -> LogViewModel(date = date) }
}