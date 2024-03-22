package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.faltenreich.diaguard.timeline.TimelineConfig

data class TimelineCoordinates(
    val canvas: Rect,
    val chart: Rect,
    val list: Rect,
    val time: Rect,
    val date: Rect,
    val scroll: Offset,
) {

    companion object {

        fun from(
            drawScope: DrawScope,
            scrollOffset: Offset,
            listItemCount: Int,
            config: TimelineConfig,
        ): TimelineCoordinates {
            val origin = Offset.Zero
            val size = drawScope.size

            val timeSize = Size(
                width = size.width,
                height = config.fontSize + config.padding * 2,
            )
            val dateSize = Size(
                width = size.width,
                height = config.fontSize * 3 + config.padding * 2,
            )
            val listItemHeight = config.fontSize + config.padding * 2
            val listSize = Size(
                width = size.width,
                height = listItemHeight * listItemCount,
            )
            val chartSize = Size(
                width = size.width,
                height = size.height - listSize.height - timeSize.height - dateSize.height,
            )

            val listOrigin = Offset(
                x = origin.x,
                y =  origin.y + chartSize.height,
            )
            val timeOrigin = Offset(
                x = origin.x,
                y = origin.y + chartSize.height + listSize.height,
            )
            val dateOrigin = Offset(
                x = origin.x,
                y = timeOrigin.y + timeSize.height,
            )

            return TimelineCoordinates(
                canvas = Rect(offset = origin, size),
                chart = Rect(offset = origin, size = chartSize),
                list = Rect(offset = listOrigin, size = listSize),
                date = Rect(offset = dateOrigin, size = dateSize),
                time = Rect(offset = timeOrigin, size = timeSize),
                scroll = scrollOffset,
            )
        }
    }
}