package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.chart.TimelineChartState

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(state: TimelineChartState) = with(state) {
    val y = size.height - padding

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xProgression = xStart .. xEnd step widthPerHour

    xProgression.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }

        val x = xOfLabel.toFloat()
        if (hour == 0) {
            val xOffsetInDays = xAbsolute / widthPerDay
            val date = initialDate.plusDays(xOffsetInDays.toInt())
            drawText(dateTimeFormatter.formatDate(date), x + padding, y - fontSize - padding, fontSize, fontPaint)
            // Hide day dividers initially
            if (offset.x != 0f) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(x = x, y = 0f),
                    end = Offset(x = x, y = size.height),
                    strokeWidth = Stroke.DefaultMiter,
                )
            }
        }
        drawText(hour.toString(), x + padding, y, fontSize, fontPaint)
        drawLine(Color.LightGray, start = Offset(x = x, y = 0f), end = Offset(x = x, y = size.height))
    }
}