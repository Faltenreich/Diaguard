package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.primitive.format
import com.faltenreich.diaguard.shared.view.drawText
import com.faltenreich.diaguard.timeline.TimelineConfig
import kotlin.math.max
import kotlin.math.min

@Suppress("FunctionName")
fun DrawScope.TimelineXAxis(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
) = with(config) {
    drawRect(
        color = gridShadowColor,
        topLeft = timeOrigin,
        size = Size(
            width = timeSize.width,
            height = timeSize.height + dateSize.height,
        ),
        style = Fill,
    )

    val widthPerDay = size.width
    val widthPerHour = (widthPerDay / xAxisLabelCount).toInt()

    val xOffset = offset.x.toInt()
    val xOfFirstHour = xOffset % widthPerHour
    val xOfLastHour = xOfFirstHour + (xAxisLabelCount * widthPerHour)
    // Paint one additional hour per side to support cut-off labels
    val (xStart, xEnd) = xOfFirstHour - widthPerHour to xOfLastHour + widthPerHour
    val xOfHours = xStart .. xEnd step widthPerHour

    xOfHours.forEach { xOfLabel ->
        val xAbsolute = -(xOffset - xOfLabel)
        val xOffsetInHours = xAbsolute / widthPerHour
        val xOffsetInHoursOfDay = ((xOffsetInHours % xAxis.last) * xAxis.step) % xAxis.last
        val hour = when {
            xOffsetInHoursOfDay < 0 -> xOffsetInHoursOfDay + xAxis.last
            else -> xOffsetInHoursOfDay
        }
        val x = xOfLabel.toFloat()
        if (hour == xAxis.first) {
            // Hide date indicator initially
            if (offset.x != 0f) {
                drawDateIndicator(config, x)
            }
        }
        drawHour(origin, size, timeOrigin, timeSize, dateOrigin, dateSize, offset, config, hour, x)
    }
    drawDates(dateOrigin, dateSize, offset, config)
}

private fun DrawScope.drawDates(
    origin: Offset,
    size: Size,
    offset: Offset,
    config: TimelineConfig,
) = with(config) {
    val isScrollingToRight = offset.x < 0
    val widthPerDay = size.width

    val xOfFirstHour =
        if (isScrollingToRight) widthPerDay + offset.x % widthPerDay
        else offset.x % widthPerDay
    val xOffsetInDays = -(offset.x / widthPerDay).toInt()

    val secondDate =
        if (isScrollingToRight) initialDate.plus(xOffsetInDays + 1, DateUnit.DAY)
        else initialDate.plus(xOffsetInDays, DateUnit.DAY)
    val firstDate = secondDate.minus(1, DateUnit.DAY)

    val firstDateAsText = "%s, %s".format(
        daysOfWeek[firstDate.dayOfWeek],
        dateTimeFormatter.formatDate(firstDate),
    )
    val secondDateAsText = "%s, %s".format(
        daysOfWeek[secondDate.dayOfWeek],
        dateTimeFormatter.formatDate(secondDate),
    )

    val firstDateTextWidth = textMeasurer.measure(firstDateAsText).size.width
    val secondDateTextWidth = textMeasurer.measure(secondDateAsText).size.width

    val xStart = padding
    val xBeforeIndicator = xOfFirstHour - firstDateTextWidth - padding - 60 // FIXME: Fix magic offset
    val xCenterOfFirstDate = xOfFirstHour - size.width / 2 - firstDateTextWidth / 2
    drawText(
        text = firstDateAsText,
        x = max(min(xStart, xBeforeIndicator), xCenterOfFirstDate),
        y = origin.y + size.height / 2 + fontSize / 2,
        size = fontSize,
        paint = fontPaint,
    )

    val xAfterIndicator = xOfFirstHour + padding
    val xCenterOfSecondDate = xOfFirstHour + size.width / 2 - secondDateTextWidth / 2
    val xEnd = size.width - secondDateTextWidth - padding * 4 // FIXME: Fix magic offset
    drawText(
        text = secondDateAsText,
        x = max(min(xCenterOfSecondDate, xEnd), xAfterIndicator),
        y = origin.y + size.height / 2 + fontSize / 2,
        size = fontSize,
        paint = fontPaint,
    )
}

private fun DrawScope.drawDateIndicator(
    config: TimelineConfig,
    x: Float,
) = with(config) {
    val gradientWidth = 40f
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            .0f to Color.Transparent,
            1f to gridShadowColor,
        ),
        startX = x - gradientWidth,
        endX = x,
    )
    drawRect(
        brush = gradient,
        topLeft = Offset(x = x - gradientWidth, y = 0f),
        size = Size(width = gradientWidth, height = size.height),
    )
}

private fun DrawScope.drawHour(
    origin: Offset,
    size: Size,
    timeOrigin: Offset,
    timeSize: Size,
    dateOrigin: Offset,
    dateSize: Size,
    offset: Offset,
    config: TimelineConfig,
    hour: Int,
    x: Float,
) = with(config) {
    val lineEndY = when (hour) {
        0 -> origin.y + size.height
        else -> origin.y + size.height - timeSize.height - dateSize.height + padding
    }
    drawLine(
        color = gridStrokeColor,
        start = Offset(x = x, y = 0f),
        end = Offset(x = x, y = lineEndY),
        strokeWidth = gridStrokeWidth,
    )
    drawText(
        text = hour.toString(),
        x = x + padding,
        y = timeOrigin.y + padding + fontSize,
        size = fontSize,
        paint = fontPaint,
    )
}