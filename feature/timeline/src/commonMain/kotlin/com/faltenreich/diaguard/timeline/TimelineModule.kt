package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.data.dataModule
import com.faltenreich.diaguard.timeline.canvas.GetTimelineCanvasDimensionsUseCase
import com.faltenreich.diaguard.timeline.canvas.TapTimelineCanvasUseCase
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
    includes(dataModule())

    factoryOf(::GetTimelineChartMeasurementPropertyUseCase)
    factoryOf(::GetTimelineChartMeasurementValuesUseCase)
    factoryOf(::TapTimelineCanvasUseCase)
    factoryOf(::GetTimelineTableMeasurementPropertiesUseCase)
    factoryOf(::GetTimelineTableMeasurementValuesUseCase)
    factoryOf(::GetTimelineCanvasDimensionsUseCase)
    factoryOf(::GetTimelineDateStateUseCase)
    factoryOf(::GetTimelineTimeStateUseCase)
    factoryOf(::GetTimelineChartStateUseCase)
    factoryOf(::GetTimelineTableStateUseCase)

    viewModelOf(::TimelineViewModel)
}