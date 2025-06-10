package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartStateUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementPropertiesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableStateUseCase
import com.faltenreich.diaguard.timeline.date.GetTimelineDateStateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timelineModule() = module {
    factoryOf(::GetTimelineTableMeasurementPropertiesUseCase)
    factoryOf(::GetTimelineChartMeasurementValuesUseCase)
    factoryOf(::GetTimelineChartStateUseCase)
    factoryOf(::GetTimelineTableStateUseCase)
    factoryOf(::GetTimelineDateStateUseCase)

    viewModelOf(::TimelineViewModel)
}