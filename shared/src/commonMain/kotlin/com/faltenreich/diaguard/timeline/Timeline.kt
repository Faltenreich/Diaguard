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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.faltenreich.diaguard.timeline.chart.TimelineCoordinates
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
            var canvasWidth by remember { mutableStateOf(0) }
            LaunchedEffect(offsetX.value) {
                val widthPerDay = canvasWidth
                val offsetInDays = ceil(offsetX.value * -1) / widthPerDay
                val date = state.initialDate.plus(offsetInDays.toInt(), DateUnit.DAY)
                viewModel.dispatchIntent(TimelineIntent.SetDate(date))
            }
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
                    .onGloballyPositioned { coordinates -> canvasWidth = coordinates.size.width }
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
                                    velocityTracker.addPosition(change.uptimeMillis, change.position)
                                    change.consume()
                                }
                            },
                            onDragEnd = {
                                scope.launch {
                                    val velocity = velocityTracker.calculateVelocity()
                                    val targetValueX = decay.calculateTargetValue(offsetX.value, velocity.x)
                                    offsetX.animateTo(
                                        targetValue = targetValueX,
                                        initialVelocity = velocity.x,
                                        animationSpec = animationSpec,
                                    )
                                    velocityTracker.resetTracking()
                                }
                            }
                        )
                    },
            ) {
                val coordinates = TimelineCoordinates.from(
                    drawScope = this,
                    scrollOffset = Offset(x = offsetX.value, y = 0f),
                    listItemCount = state.propertiesForList.size,
                    config = config,
                )
                TimelineXAxis(coordinates, config)
                TimelineChart(coordinates, config, state.valuesForChart)
                TimelineYAxis(coordinates, config)
                TimelineList(coordinates, config, state.propertiesForList, state.valuesForList)
            }
        }
    }
}