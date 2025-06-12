package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class TimelineCanvasDimensions(
    val canvas: Rect,
    val chart: Rect,
    val table: Rect,
    val time: Rect,
    val scroll: Float,
) {

    companion object {

        fun from(
            canvasSize: Size?,
            tableRowHeight: Float,
            scrollOffset: Float,
            properties: List<MeasurementProperty>,
        ): TimelineCanvasDimensions? {
            if (canvasSize == null) {
                return null
            }

            val origin = Offset.Zero

            val timeSize = Size(
                width = canvasSize.width,
                height = tableRowHeight,
            )
            val tableSize = Size(
                width = canvasSize.width,
                height = tableRowHeight * properties.size,
            )
            val chartSize = Size(
                width = canvasSize.width,
                height = canvasSize.height - tableSize.height - timeSize.height,
            )

            val tableOrigin = Offset(
                x = origin.x,
                y =  origin.y + chartSize.height,
            )
            val timeOrigin = Offset(
                x = origin.x,
                y = origin.y + chartSize.height + tableSize.height,
            )

            return TimelineCanvasDimensions(
                canvas = Rect(offset = origin, size = canvasSize),
                chart = Rect(offset = origin, size = chartSize),
                table = Rect(offset = tableOrigin, size = tableSize),
                time = Rect(offset = timeOrigin, size = timeSize),
                scroll = scrollOffset,
            )
        }
    }
}