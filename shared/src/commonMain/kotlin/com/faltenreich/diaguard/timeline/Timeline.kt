package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.datetime.DateUnit
import com.faltenreich.diaguard.shared.datetime.DayOfWeek
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.theme.LocalDimensions
import com.faltenreich.diaguard.shared.theme.color.LocalColors
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.chart.TimelineList
import com.faltenreich.diaguard.timeline.chart.TimelineXAxis
import com.faltenreich.diaguard.timeline.chart.TimelineYAxis
import kotlin.math.ceil

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    when (val state = viewModel.collectState()) {
        null -> Unit
        else -> {
            // TODO: Reset remember when initialDate changes
            var offset by remember { mutableStateOf(Offset.Zero) }

            val density = LocalDensity.current
            val colors = LocalColors.current
            val dimensions = LocalDimensions.current
            val typography = AppTheme.typography

            val config = TimelineConfig(
                initialDate = state.initialDate,
                daysOfWeek = DayOfWeek.entries.associateWith { getString(it.abbreviation) },
                textMeasurer = rememberTextMeasurer(),
                dateTimeFormatter = dateTimeFormatter,
                padding = density.run { dimensions.padding.P_2.toPx() },
                fontPaint = Paint().apply { color = colors.scheme.onBackground },
                fontSize = density.run { typography.bodyMedium.fontSize.toPx() },
                backgroundColor = colors.scheme.background,
                gridStrokeColor = colors.scheme.onSurfaceVariant,
                gridShadowColor = colors.scheme.surfaceVariant,
                valueColorNormal = colors.ValueNormal,
                valueColorLow = colors.ValueLow,
                valueColorHigh = colors.ValueHigh,
            )
            Canvas(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { _, dragAmount ->
                                offset += dragAmount * 1.5f

                                val widthPerDay = size.width
                                val offsetInDays = ceil(offset.x * -1) / widthPerDay
                                val date = state.initialDate.plus(offsetInDays.toInt(), DateUnit.DAY)
                                viewModel.dispatchIntent(TimelineIntent.SetDate(date))
                            },
                        )
                    },
            ) {
                val origin = Offset.Zero
                val size = size

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
                    height = listItemHeight * state.propertiesForList.size,
                )
                val chartSize = Size(
                    width = size.width,
                    height = size.height - listSize.height - timeSize.height - dateSize.height,
                )

                val chartOrigin = origin
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

                // TODO: Draw values above grid but below axis
                TimelineXAxis(
                    origin = origin,
                    size = size,
                    timeOrigin = timeOrigin,
                    timeSize = timeSize,
                    dateOrigin = dateOrigin,
                    dateSize = dateSize,
                    offset = offset,
                    config = config,
                )
                TimelineChart(
                    origin = chartOrigin,
                    size = chartSize,
                    offset = offset,
                    config = config,
                    values = state.valuesForChart,
                )
                TimelineYAxis(
                    origin = chartOrigin,
                    size = chartSize,
                    config = config,
                )
                TimelineList(
                    origin = listOrigin,
                    size = listSize,
                    config = config,
                    properties = state.propertiesForList,
                )
            }
        }
    }
}