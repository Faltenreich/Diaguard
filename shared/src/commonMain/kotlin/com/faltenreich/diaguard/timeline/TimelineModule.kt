package com.faltenreich.diaguard.timeline

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timelineModule() = module {
    singleOf(::GetMeasurementValuesAroundDateUseCase)
    singleOf(::GetTimelineDataUseCase)
    singleOf(::FormatTimelineDateUseCase)

    viewModelOf(::TimelineViewModel)
}