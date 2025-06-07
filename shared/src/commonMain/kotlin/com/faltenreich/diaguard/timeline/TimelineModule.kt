package com.faltenreich.diaguard.timeline

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timelineModule() = module {
    factoryOf(::GetTimelineDataUseCase)
    factoryOf(::FormatTimelineDateUseCase)

    viewModelOf(::TimelineViewModel)
}