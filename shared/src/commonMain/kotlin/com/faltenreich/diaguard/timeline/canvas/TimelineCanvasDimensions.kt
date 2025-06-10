package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

data class TimelineCanvasDimensions(
    val canvas: Rect,
    val chart: Rect,
    val table: Rect,
    val time: Rect,
) {

    companion object {

        val Zero = TimelineCanvasDimensions(
            canvas = Rect.Zero,
            chart = Rect.Zero,
            table = Rect.Zero,
            time = Rect.Zero,
        )

        fun from(
            size: Size,
            tableRowCount: Int,
            tableRowHeight: Float,
        ): TimelineCanvasDimensions {
            val origin = Offset.Zero

            val timeSize = Size(
                width = size.width,
                height = tableRowHeight,
            )
            val tableSize = Size(
                width = size.width,
                height = tableRowHeight * tableRowCount,
            )
            val chartSize = Size(
                width = size.width,
                height = size.height - tableSize.height - timeSize.height,
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
                canvas = Rect(offset = origin, size = size),
                chart = Rect(offset = origin, size = chartSize),
                table = Rect(offset = tableOrigin, size = tableSize),
                time = Rect(offset = timeOrigin, size = timeSize),
            )
        }
    }
}