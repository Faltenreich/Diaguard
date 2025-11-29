package com.faltenreich.diaguard.timeline.canvas.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.DateTimeConstants
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.timeline.canvas.TimelineCanvasDimensions
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTimeState
import kotlin.math.ceil
import kotlin.math.max

class GetTimelineChartStateUseCase(private val mapValue: MeasurementValueMapper) {

    operator fun invoke(
        values: List<MeasurementValue.Local>,
        property: MeasurementProperty.Local,
        decimalPlaces: Int,
        time: TimelineTimeState?,
        dimensions: TimelineCanvasDimensions.Calculated?,
    ): TimelineChartState? {
        if (time == null || dimensions == null) {
            return null
        }

        val rectangle = dimensions.chart

        val iconRectangle = Rect(
            offset = Offset(
                x = rectangle.left,
                y = rectangle.bottom - dimensions.tableRowHeight,
            ),
            size = Size(
                width = dimensions.tableRowHeight,
                height = dimensions.tableRowHeight,
            ),
        )

        val stepCount = time.hourProgression.last / time.hourProgression.step

        val valuesWithXCoordinate = values.map { value ->
            val dateTime = value.entry.dateTime
            val xAxisStep = DateTimeConstants.HOURS_PER_DAY / stepCount
            val widthPerDay = dimensions.chart.size.width
            val widthPerHour = widthPerDay / stepCount
            val widthPerMinute = widthPerHour / DateTimeConstants.MINUTES_PER_HOUR
            val offsetInMinutes = time.initialDateTime.minutesUntil(dateTime)
            val offsetOfDateTime = (offsetInMinutes / xAxisStep) * widthPerMinute
            value to dimensions.chart.topLeft.x + dimensions.scroll + offsetOfDateTime
        }

        val valueMin = Y_AXIS_MIN
        val valueMax = valuesWithXCoordinate
            .partition { (_, x) -> x in rectangle.left .. rectangle.right }
            .let { (visible, invisible) ->
                // Scroll either to maximum value in viewport, nearest neighbour or Y_AXIS_MAX_MIN
                val valueMaxValueVisible = visible.maxOfOrNull { (value, _) -> value.value } ?: 0.0
                val valueNearestValueInvisible = listOfNotNull(
                    invisible.filter { (_, x) -> x < rectangle.left }.maxByOrNull { (_, x) -> x },
                    invisible.filter { (_, x) -> x > rectangle.right }.minByOrNull { (_, x) -> x },
                ).maxByOrNull { (value, _) -> value.value }?.let { (value, x) ->
                    // Transition to nearest neighbour via its distance to viewport
                    val valueMaxDistance =
                        if (x < rectangle.left) rectangle.left - x
                        else if (x > rectangle.right) x - rectangle.right
                        else 0f
                    value.value - valueMaxDistance
                } ?: 0.0
                max(Y_AXIS_MAX_MIN, max(valueMaxValueVisible, valueNearestValueInvisible))
            }
        val valueStep = Y_AXIS_VALUE_STEP
        val valueMaxPadded = ceil((valueMax + valueStep) / valueStep) * valueStep
        val valueAxis = valueMin.toInt() .. valueMaxPadded.toInt() step valueStep.toInt()

        val heightPerSection = (rectangle.size.height / (valueAxis.last() / valueStep)).toInt()
        val labels = valueAxis
            .drop(1)
            .dropLast(1)
            .map { value ->
                val index = valueAxis.indexOf(value)
                val y = rectangle.bottom - (index * heightPerSection)
                TimelineChartState.Label(
                    position = Offset(
                        x = rectangle.left,
                        y = y,
                    ),
                    text = mapValue(
                        value = value.toDouble(),
                        property = property,
                        decimalPlaces = decimalPlaces,
                    ).value,
                )
            }

        val items = valuesWithXCoordinate.map { (value, x) ->
            val percentage = (value.value - valueAxis.first()) / (valueAxis.last() - valueAxis.first())
            val y = dimensions.chart.topLeft.y +
                dimensions.chart.size.height -
                (percentage.toFloat() * dimensions.chart.size.height)
            TimelineChartState.Item(
                value = value,
                position = Offset(x, y),
            )
        }

        val colorStops = mutableListOf<TimelineChartState.ColorStop>()
        val valueLow = property.range.low?.takeIf { property.range.isHighlighted }
        val valueHigh = property.range.high?.takeIf { property.range.isHighlighted }
        valueHigh?.let {
            val yHighFraction = (valueHigh - valueMin).toFloat() / (valueMax - valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.HIGH))
            colorStops.add(TimelineChartState.ColorStop(1 - yHighFraction, TimelineChartState.ColorStop.Type.NORMAL))
        }
        valueLow?.let {
            val yLowFraction = (valueLow - valueMin).toFloat() / (valueMax - valueMin).toFloat()
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.NORMAL))
            colorStops.add(TimelineChartState.ColorStop(1 - yLowFraction, TimelineChartState.ColorStop.Type.LOW))
        }
        if (colorStops.isEmpty()) {
            colorStops.add(TimelineChartState.ColorStop(0f, TimelineChartState.ColorStop.Type.NONE))
            colorStops.add(TimelineChartState.ColorStop(1f, TimelineChartState.ColorStop.Type.NONE))
        }

        return TimelineChartState(
            chartRectangle = rectangle,
            iconRectangle = iconRectangle,
            property = property,
            items = items,
            colorStops = colorStops,
            labels = labels,
        )
    }

    companion object {

        private const val Y_AXIS_MIN = 0.0
        private const val Y_AXIS_MAX_MIN = 150.0
        private const val Y_AXIS_VALUE_STEP = 50.0
    }
}