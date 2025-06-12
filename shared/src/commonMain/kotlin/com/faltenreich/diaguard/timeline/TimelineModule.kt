package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementPropertyUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.chart.GetTimelineChartStateUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementPropertiesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableMeasurementValuesUseCase
import com.faltenreich.diaguard.timeline.canvas.table.GetTimelineTableStateUseCase
import com.faltenreich.diaguard.timeline.canvas.time.GetTimelineTimeStateUseCase
import com.faltenreich.diaguard.timeline.date.GetTimelineDateStateUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun timelineModule() = module {
    factoryOf(::GetTimelineChartMeasurementPropertyUseCase)
    factoryOf(::GetTimelineChartMeasurementValuesUseCase)
    factoryOf(::GetTimelineTableMeasurementPropertiesUseCase)
    factoryOf(::GetTimelineTableMeasurementValuesUseCase)
    factoryOf(::GetTimelineDateStateUseCase)
    factoryOf(::GetTimelineTimeStateUseCase)
    factoryOf(::GetTimelineChartStateUseCase)
    factoryOf(::GetTimelineTableStateUseCase)

    viewModelOf(::TimelineViewModel)
}