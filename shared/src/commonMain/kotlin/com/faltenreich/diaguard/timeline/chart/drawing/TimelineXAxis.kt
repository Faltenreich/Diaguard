package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.chart.TimelineChartConfig
import com.faltenreich.diaguard.timeline.chart.TimelineChartState

class TimelineXAxis(
    private val config: TimelineChartConfig,
    private val dateTimeFormatter: DateTimeFormatter = inject(),
) {

    fun drawOn(drawScope: DrawScope, state: TimelineChartState) {
        drawScope.drawAxis(state.offset, state.initialDate)
    }

    private fun DrawScope.drawAxis(
        offset: Offset,
        initialDate: Date,
    ) = with(config) {
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
                drawDate(date, x, offset, config)
            }
            drawHour(hour, x, config)
        }
    }

    private fun DrawScope.drawDate(
        date: Date,
        x: Float,
        offset: Offset,
        config: TimelineChartConfig,
    ) = with(config) {
        drawText(
            text = dateTimeFormatter.formatDate(date),
            x = x + padding,
            y= padding + fontSize,
            size = fontSize,
            paint = fontPaint,
        )
        if (offset.x != 0f) {
            drawLine(
                color = Color.Gray,
                start = Offset(x = x, y = 0f),
                end = Offset(x = x, y = size.height),
                strokeWidth = Stroke.DefaultMiter,
            )
        }
    }

    private fun DrawScope.drawHour(hour: Int, x: Float, config: TimelineChartConfig) = with(config) {
        drawText(
            text = hour.toString(),
            x = x + padding,
            y = size.height - padding,
            size = fontSize,
            paint = fontPaint,
        )
        drawLine(
            color = Color.LightGray,
            start = Offset(x = x, y = 0f),
            end = Offset(x = x, y = size.height),
        )
    }
}