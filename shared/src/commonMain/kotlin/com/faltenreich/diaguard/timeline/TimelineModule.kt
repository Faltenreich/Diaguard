package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartDataUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timelineModule() = module {
    factoryOf(::GetTimelineChartDataUseCase)
    factoryOf(::GetTimelineTableDataUseCase)
    factoryOf(::FormatTimelineDateUseCase)

    viewModelOf(::TimelineViewModel)
}