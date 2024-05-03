package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.timeline.TimelineConfig

data class TimelineCoordinates(
    val canvas: Rect,
    val chart: Rect,
    val list: Rect,
    val time: Rect,
    val scroll: Offset,
) {

    companion object {

        fun from(
            size: Size,
            scrollOffset: Offset,
            listItemCount: Int,
            config: TimelineConfig,
        ): TimelineCoordinates {
            val origin = Offset.Zero

            val timeSize = Size(
                width = size.width,
                height = config.fontSize + config.padding * 2,
            )
            val listItemHeight = config.fontSize + config.padding * 2
            val listSize = Size(
                width = size.width,
                height = listItemHeight * listItemCount,
            )
            val chartSize = Size(
                width = size.width,
                height = size.height - listSize.height - timeSize.height,
            )

            val listOrigin = Offset(
                x = origin.x,
                y =  origin.y + chartSize.height,
            )
            val timeOrigin = Offset(
                x = origin.x,
                y = origin.y + chartSize.height + listSize.height,
            )

            return TimelineCoordinates(
                canvas = Rect(offset = origin, size),
                chart = Rect(offset = origin, size = chartSize),
                list = Rect(offset = listOrigin, size = listSize),
                time = Rect(offset = timeOrigin, size = timeSize),
                scroll = scrollOffset,
            )
        }
    }
}