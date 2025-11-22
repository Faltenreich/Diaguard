package com.faltenreich.diaguard.statistic.trend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.measurement.value.MeasurementValueTint
import com.faltenreich.diaguard.data.preview.AppPreview
import com.faltenreich.diaguard.statistic.trend.StatisticTrendState.Interval
import org.jetbrains.compose.ui.tooling.preview.Preview

const val VALUE_DOT_RADIUS = 12f

@Composable
fun StatisticTrendChart(
    state: StatisticTrendState,
    modifier: Modifier = Modifier,
) = with(state) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val fontColor = AppTheme.colors.scheme.onSurfaceVariant
    val fontSize = AppTheme.typography.bodyMedium.fontSize
    val textStyle = TextStyle(fontSize = fontSize, color = fontColor)
    val padding = density.run { AppTheme.dimensions.padding.P_2.toPx() }

    val colorTintNone = AppTheme.colors.scheme.onPrimary
    val colorTintLow = AppTheme.colors.ValueLow
    val colorTintNormal = AppTheme.colors.ValueNormal
    val colorTintHigh = AppTheme.colors.ValueHigh
    val getColor = { tint: MeasurementValueTint ->
        when (tint) {
            MeasurementValueTint.NONE -> colorTintNone
            MeasurementValueTint.LOW -> colorTintLow
            MeasurementValueTint.NORMAL -> colorTintNormal
            MeasurementValueTint.HIGH -> colorTintHigh
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val widthPerDay = size.width / intervals.size
        val textSize = textMeasurer.measure("D", textStyle)
        val textHeight = textSize.size.height.toFloat()

        intervals.forEachIndexed { index, day ->
            val x = index * widthPerDay
            val labelRectangle = Rect(
                offset = Offset(
                    x = x,
                    y = size.height - textHeight,
                ),
                size = Size(
                    width = widthPerDay,
                    height = textHeight,
                ),
            )
            drawLabel(
                interval = day,
                rectangle = labelRectangle,
                textStyle = textStyle,
                textMeasurer = textMeasurer,
            )

            val chartRectangle = Rect(
                offset = Offset(
                    x = x,
                    y = padding,
                ),
                size = Size(
                    width = widthPerDay,
                    height = size.height - textHeight - padding - padding,
                ),
            )
            drawTarget(
                target = targetValue,
                maximum = maximumValue,
                rectangle = chartRectangle,
            )
            day.average?.let { value ->
                drawValue(
                    value = value.value,
                    color = getColor(value.tint),
                    maximum = maximumValue,
                    rectangle = chartRectangle,
                    radius = VALUE_DOT_RADIUS,
                )
            }
        }
    }
}

private fun DrawScope.drawLabel(
    interval: Interval,
    rectangle: Rect,
    textStyle: TextStyle,
    textMeasurer: TextMeasurer,
) {
    val text = interval.label
    val textSize = textMeasurer.measure(text, textStyle).size.toSize()
    drawText(
        textMeasurer = textMeasurer,
        text = text,
        topLeft = Offset(
            x = rectangle.center.x - (textSize.width / 2),
            y = rectangle.topCenter.y,
        ),
        style = textStyle,
        size = textSize,
    )
}

private fun DrawScope.drawTarget(
    target: Double,
    maximum: Double,
    rectangle: Rect,
) {
    val y = rectangle.top + rectangle.height - (rectangle.height * (target / maximum).toFloat())
    drawLine(
        color = Color.Gray,
        start = Offset(x = rectangle.left, y = y),
        end = Offset(x = rectangle.right, y = y),
    )
}

private fun DrawScope.drawValue(
    value: Double,
    color: Color,
    maximum: Double,
    rectangle: Rect,
    radius: Float,
) {
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(
            x = rectangle.center.x,
            y = rectangle.top + rectangle.height - (rectangle.height * (value / maximum).toFloat()),
        ),
        style = Fill,
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    StatisticTrendChart(
        state = StatisticTrendState(
            intervals = week().map { date ->
                Interval(
                    dateRange = date .. date,
                    label = date.dayOfWeek.localized(),
                    average = null,
                )
            },
            targetValue = 120.0,
            maximumValue = 200.0,
        ),
    )
}