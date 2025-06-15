package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

class GetTimelineCanvasDimensionsUseCase {

    operator fun invoke(
        setup: TimelineCanvasSetup?,
        scrollOffset: Float,
        properties: List<MeasurementProperty>,
    ): TimelineCanvasDimensions? {
        if (setup == null) {
            return null
        }

        val origin = Offset.Zero

        val statusBar = Rect(
            offset = origin,
            size = Size(
                width = setup.canvasSize.width,
                height = setup.statusBarHeight.toFloat(),
            ),
        )

        val tableSize = Size(
            width = setup.canvasSize.width,
            height = setup.tableRowHeight * properties.size,
        )

        val timeSize = Size(
            width = setup.canvasSize.width,
            height = setup.tableRowHeight,
        )

        val chart = Rect(
            offset = Offset(
                x = origin.x,
                y = origin.y + statusBar.height,
            ),
            size = Size(
                width = setup.canvasSize.width,
                height = setup.canvasSize.height - setup.statusBarHeight - tableSize.height - timeSize.height,
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

        return TimelineCanvasDimensions(
            canvas = Rect(offset = origin, size = setup.canvasSize),
            statusBar = statusBar,
            chart = chart,
            table = Rect(offset = tableOrigin, size = tableSize),
            time = Rect(offset = timeOrigin, size = timeSize),
            scroll = scrollOffset,
        )
    }
}