package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.entry.list.MapEntryListItemStateUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValue

class TapTimelineCanvasUseCase(private val mapEntryListItemState: MapEntryListItemStateUseCase) {

    suspend operator fun invoke(
        position: Offset,
        touchAreaSize: Size,
        canvas: TimelineCanvasState,
    ): TapTimelineCanvasResult {
        val touchArea = Rect(
            offset = Offset(
                x = position.x - touchAreaSize.width / 2,
                y = position.y - touchAreaSize.height / 2,
            ),
            size = touchAreaSize,
        )
        return if (canvas.chart.chartRectangle.contains(position)) {
            when (val item = canvas.chart.items.firstOrNull { touchArea.contains(it.position) }) {
                null -> TapTimelineCanvasResult.None
                else -> TapTimelineCanvasResult.Chart(item.value.toEntryListItemState())
            }
        } else if (touchArea.overlaps(canvas.table.rectangle)) {
            val values = canvas.table.categories.flatMap { category ->
                category.properties.flatMap { property ->
                    property.values
                }
            }
            when (val item = values.firstOrNull { touchArea.overlaps(it.rectangle) }) {
                null -> TapTimelineCanvasResult.None
                else -> TapTimelineCanvasResult.Table(item.values.map { it.toEntryListItemState() })
            }
        } else {
            TapTimelineCanvasResult.None
        }
    }

    private suspend fun MeasurementValue.Local.toEntryListItemState(): EntryListItemState {
        return mapEntryListItemState(
            entry = entry,
            includeDate = false,
        )
    }
}