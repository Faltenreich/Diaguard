package com.faltenreich.diaguard.timeline

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
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
import kotlinx.coroutines.launch
import kotlin.math.ceil

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
    dateTimeFormatter: DateTimeFormatter = inject(),
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val colors = LocalColors.current
    val colorScheme = colors.scheme
    val dimensions = LocalDimensions.current
    val typography = AppTheme.typography
    val textMeasurer = rememberTextMeasurer()

    when (val state = viewModel.collectState()) {
        null -> Unit
        else -> {
            // TODO: Reset remember when initialDate changes
            val offsetX = remember { Animatable(0f) }
            val offsetY = remember { Animatable(0f) }
            val config by remember {
                val config = TimelineConfig(
                    initialDate = state.initialDate,
                    daysOfWeek = DayOfWeek.entries.associateWith { getString(it.abbreviation) },
                    textMeasurer = textMeasurer,
                    dateTimeFormatter = dateTimeFormatter,
                    padding = density.run { dimensions.padding.P_2.toPx() },
                    fontPaint = Paint().apply { color = colorScheme.onBackground },
                    fontSize = density.run { typography.bodyMedium.fontSize.toPx() },
                    backgroundColor = colorScheme.background,
                    gridStrokeColor = colorScheme.onSurfaceVariant,
                    gridShadowColor = colorScheme.surfaceVariant,
                    valueColorNormal = colors.ValueNormal,
                    valueColorLow = colors.ValueLow,
                    valueColorHigh = colors.ValueHigh,
                )
                mutableStateOf(config)
            }

            Canvas(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        val decay = splineBasedDecay<Float>(this)
                        val animationSpec = FloatSpringSpec(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessVeryLow,
                        )
                        val velocityTracker = VelocityTracker()
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                    velocityTracker.addPosition(change.uptimeMillis, change.position)

                                    val widthPerDay = size.width
                                    val offsetInDays = ceil(offsetX.value * -1) / widthPerDay
                                    val date = state.initialDate.plus(offsetInDays.toInt(), DateUnit.DAY)
                                    viewModel.dispatchIntent(TimelineIntent.SetDate(date))

                                    change.consume()
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val velocity = velocityTracker.calculateVelocity()
                                    val targetValueX = decay.calculateTargetValue(offsetX.value, velocity.x)
                                    val targetValueY = decay.calculateTargetValue(offsetY.value, velocity.y)
                                    offsetX.animateTo(
                                        targetValue = targetValueX,
                                        initialVelocity = velocity.x,
                                        animationSpec = animationSpec,
                                    )
                                    offsetY.animateTo(
                                        targetValue = targetValueY,
                                        initialVelocity = velocity.y,
                                        animationSpec = animationSpec,
                                    )
                                    velocityTracker.resetTracking()
                                }
                            }
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
                val offset = Offset(x = offsetX.value, y = offsetY.value)
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