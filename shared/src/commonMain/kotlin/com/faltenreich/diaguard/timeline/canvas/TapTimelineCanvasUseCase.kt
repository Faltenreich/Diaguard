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
        // TODO: Reduce computations by merging interactive rectangles before traversal
        // TODO: Search for largest overlap to avoid miss-taps due to large touch area
        return if (canvas.chart.chartRectangle.contains(position)) {
            val items = canvas.chart.items.filter { touchArea.contains(it.position) }
            if (items.isEmpty()) {
                TapTimelineCanvasResult.None
            } else {
                TapTimelineCanvasResult.Chart(items.map { it.value.toEntryListItemState() })
            }
        } else if (touchArea.overlaps(canvas.table.rectangle)) {
            val properties = canvas.table.categories.flatMap { it.properties }
            when (val property = properties.firstOrNull { touchArea.overlaps(it.iconRectangle) }) {
                null -> {
                    val values = properties.flatMap { it.values }
                    when (val item = values.firstOrNull { touchArea.overlaps(it.rectangle) }) {
                        null -> TapTimelineCanvasResult.None
                        else -> TapTimelineCanvasResult.Table(item.values.map { it.toEntryListItemState() })
                    }
                }
                else -> TapTimelineCanvasResult.Icon(property.property)
            }

        } else {
            TapTimelineCanvasResult.None
        }
    }

    private suspend fun MeasurementValue.Local.toEntryListItemState(): EntryListItemState {
        return mapEntryListItemState(entry, includeDate = false)
    }
}