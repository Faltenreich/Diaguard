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
        return if (canvas.chart.chartRectangle.contains(position)) {
            if (touchArea.overlaps(canvas.chart.iconRectangle)) {
                TapTimelineCanvasResult.Icon(canvas.chart.property)
            } else {
                val items = canvas.chart.items.filter { touchArea.contains(it.position) }
                if (items.isEmpty()) {
                    TapTimelineCanvasResult.None
                } else {
                    TapTimelineCanvasResult.Chart(items.map { it.value.toEntryListItemState() })
                }
            }
        } else if (touchArea.overlaps(canvas.table.rectangle)) {
            val properties = canvas.table.categories.flatMap { it.properties }
            val property = properties
                .mapNotNull { property ->
                    val intersection = touchArea.intersectComparable(property.iconRectangle)
                        ?: return@mapNotNull null
                    property to intersection
                }
                .maxByOrNull { (_, intersection) -> intersection }
                ?.first

            when (property) {
                null -> {
                    val values = properties.flatMap { it.values }
                    val value = values
                        .mapNotNull { value ->
                            val intersection = touchArea.intersectComparable(value.rectangle)
                                ?: return@mapNotNull null
                            value to intersection
                        }
                        .maxByOrNull { (_, intersection) -> intersection }
                        ?.first
                    when (value) {
                        null -> TapTimelineCanvasResult.None
                        else -> TapTimelineCanvasResult.Table(value.values.map { it.toEntryListItemState() })
                    }
                }
                else -> TapTimelineCanvasResult.Icon(property.property)
            }

        } else {
            TapTimelineCanvasResult.None
        }
    }

    private fun Rect.intersectComparable(other: Rect): Float? {
        return intersect(other)
            .takeUnless(Rect::isEmpty)
            ?.run { width + height }
    }

    private suspend fun MeasurementValue.Local.toEntryListItemState(): EntryListItemState {
        return mapEntryListItemState(entry, includeDate = false)
    }
}