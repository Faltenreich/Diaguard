package com.faltenreich.diaguard.timeline

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun timelineModule() = module {
    singleOf(::FormatTimelineDateUseCase)
    singleOf(::TimelineViewModel)
}