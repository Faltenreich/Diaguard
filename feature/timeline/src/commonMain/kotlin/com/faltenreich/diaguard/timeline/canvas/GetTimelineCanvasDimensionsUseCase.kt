package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty

class GetTimelineCanvasDimensionsUseCase {

    operator fun invoke(
        dimensions: TimelineCanvasDimensions.Positioned?,
        scrollOffset: Float,
        properties: List<MeasurementProperty>,
    ): TimelineCanvasDimensions.Calculated? = dimensions?.run {
        val origin = Offset.Zero

        val statusBar = Rect(
            offset = origin,
            size = Size(
                width = canvasSize.width,
                height = statusBarHeight.toFloat(),
            ),
        )

        val tableSize = Size(
            width = canvasSize.width,
            height = tableRowHeight * properties.size,
        )

        val timeSize = Size(
            width = canvasSize.width,
            height = tableRowHeight,
        )

        val chart = Rect(
            offset = Offset(
                x = origin.x,
                y = origin.y + statusBar.height,
            ),
            size = Size(
                width = canvasSize.width,
                height = canvasSize.height - statusBarHeight - tableSize.height - timeSize.height,
            ),
        )

        val tableOrigin = Offset(
            x = origin.x,
            y =  chart.top + chart.height,
        )
        val timeOrigin = Offset(
            x = origin.x,
            y = tableOrigin.y + tableSize.height,
        )

        return TimelineCanvasDimensions.Calculated(
            canvas = Rect(offset = origin, size = canvasSize),
            statusBar = statusBar,
            chart = chart,
            table = Rect(offset = tableOrigin, size = tableSize),
            tableRowHeight = tableRowHeight,
            time = Rect(offset = timeOrigin, size = timeSize),
            scroll = scrollOffset,
        )
    }
}