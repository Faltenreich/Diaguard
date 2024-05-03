package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.TimelineConfig

data class TimelineCoordinates(
    val canvas: Rect,
    val chart: Rect,
    val table: Rect,
    val time: Rect,
    val scroll: Offset,
) {

    companion object {

        fun from(
            size: Size,
            scrollOffset: Offset,
            tableRowCount: Int,
            config: TimelineConfig,
        ): TimelineCoordinates {
            val origin = Offset.Zero

            val timeSize = Size(
                width = size.width,
                height = config.fontSize + config.padding * 2,
            )
            val tableItemHeight = config.fontSize + config.padding * 2
            val tableSize = Size(
                width = size.width,
                height = tableItemHeight * tableRowCount,
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

            return TimelineCoordinates(
                canvas = Rect(offset = origin, size),
                chart = Rect(offset = origin, size = chartSize),
                table = Rect(offset = tableOrigin, size = tableSize),
                time = Rect(offset = timeOrigin, size = timeSize),
                scroll = scrollOffset,
            )
        }
    }
}