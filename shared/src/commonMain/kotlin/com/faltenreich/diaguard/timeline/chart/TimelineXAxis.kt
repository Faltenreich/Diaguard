package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextMeasurer
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import kotlin.math.max
import kotlin.math.min

private const val GRADIENT_WIDTH = 40f
// FIXME: Fix magic offset
private const val X_BEFORE_INDICATOR_OFFSET = 60

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    drawRect(
        color = config.gridShadowColor,
        topLeft = coordinates.time.topLeft,
        size = Size(
            width = coordinates.time.size.width,
            height = coordinates.time.size.height + coordinates.date.size.height,
        ),
        style = Fill,
    )

    val widthPerDay = coordinates.canvas.size.width
    val widthPerHour = (widthPerDay / config.xAxisLabelCount).toInt()

    val xOffset = coordinates.scroll.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (config.xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xOfHours = xStart .. xEnd step widthPerHour

    xOfHours.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % config.xAxis.last) * config.xAxis.step) % config.xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + config.xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        // Hide date indicator initially
        if (hour == config.xAxis.first && coordinates.scroll.x != 0f) {
            drawDateIndicator(x, config)
        }
        drawHour(x, hour, coordinates, config)
    }
    drawDates(initialDate, coordinates, config, textMeasurer)
}

private fun DrawScope.drawDates(
    initialDate: Date,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
    textMeasurer: TextMeasurer,
) {
    val dateTimeFormatter = inject<DateTimeFormatter>()
    val isScrollingToRight = coordinates.scroll.x < 0
    val widthPerDay = coordinates.date.size.width

    val xOfFirstHour =
        if (isScrollingToRight) widthPerDay + coordinates.scroll.x % widthPerDay
        else coordinates.scroll.x % widthPerDay
    val xOffsetInDays = -(coordinates.scroll.x / widthPerDay).toInt()

    val secondDate =
        if (isScrollingToRight) initialDate.plus(xOffsetInDays + 1, DateUnit.DAY)
        else initialDate.plus(xOffsetInDays, DateUnit.DAY)
    val firstDate = secondDate.minus(1, DateUnit.DAY)

    val firstDateAsText = "%s, %s".format(
        config.daysOfWeek[firstDate.dayOfWeek],
        dateTimeFormatter.formatDate(firstDate),
    )
    val secondDateAsText = "%s, %s".format(
        config.daysOfWeek[secondDate.dayOfWeek],
        dateTimeFormatter.formatDate(secondDate),
    )

    val firstDateTextWidth = textMeasurer.measure(firstDateAsText).size.width
    val secondDateTextWidth = textMeasurer.measure(secondDateAsText).size.width

    val xStart = config.padding
    val xBeforeIndicator = xOfFirstHour - firstDateTextWidth - config.padding - X_BEFORE_INDICATOR_OFFSET
    val xCenterOfFirstDate = xOfFirstHour - coordinates.date.size.width / 2 - firstDateTextWidth / 2
    drawText(
        text = firstDateAsText,
        x = max(min(xStart, xBeforeIndicator), xCenterOfFirstDate),
        y = coordinates.date.topLeft.y + coordinates.date.size.height / 2 + config.fontSize / 2,
        size = config.fontSize,
        paint = config.fontPaint,
    )

    val xAfterIndicator = xOfFirstHour + config.padding
    val xCenterOfSecondDate = xOfFirstHour + coordinates.date.size.width / 2 - secondDateTextWidth / 2
    val xEnd = coordinates.date.size.width - secondDateTextWidth - config.padding * 2 * 2 // FIXME: Fix magic offset
    drawText(
        text = secondDateAsText,
        x = max(min(xCenterOfSecondDate, xEnd), xAfterIndicator),
        y = coordinates.date.topLeft.y + coordinates.date.size.height / 2 + config.fontSize / 2,
        size = config.fontSize,
        paint = config.fontPaint,
    )
}

private fun DrawScope.drawDateIndicator(
    x: Float,
    config: TimelineConfig,
) {
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            .0f to Color.Transparent,
            1f to config.gridShadowColor,
        ),
        startX = x - GRADIENT_WIDTH,
        endX = x,
    )
    drawRect(
        brush = gradient,
        topLeft = Offset(x = x - GRADIENT_WIDTH, y = 0f),
        size = Size(width = GRADIENT_WIDTH, height = size.height),
    )
}

private fun DrawScope.drawHour(
    x: Float,
    hour: Int,
    coordinates: TimelineCoordinates,
    config: TimelineConfig,
) {
    val lineEndY = when (hour) {
        0 -> coordinates.canvas.topLeft.y + coordinates.canvas.size.height
        else -> coordinates.canvas.topLeft.y +
                coordinates.canvas.size.height -
                coordinates.time.size.height -
                coordinates.date.size.height +
                config.padding
    }
    drawLine(
        color = config.gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = lineEndY),
        strokeWidth = config.gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + config.padding,
        y = coordinates.time.topLeft.y + config.padding + config.fontSize,
        size = config.fontSize,
        paint = config.fontPaint,
    )
}