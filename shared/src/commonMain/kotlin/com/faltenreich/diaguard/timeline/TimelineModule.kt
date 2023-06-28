package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.chart.TimelineChartViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun timelineModule() = module {
    singleOf(::TimelineChartViewModel)
    singleOf(::TimelineViewModel)
}