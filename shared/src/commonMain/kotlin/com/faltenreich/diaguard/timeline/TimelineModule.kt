package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import org.koin.dsl.module

fun timelineModule() = module {
    factory { (date: Date?) -> TimelineViewModel(date = date) }
}