package com.faltenreich.diaguard.timeline.canvas

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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.DayOfWeek
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.theme.LocalDimensions
import com.faltenreich.diaguard.shared.theme.color.LocalColors
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineEvent
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineState
import com.faltenreich.diaguard.timeline.TimelineViewModel
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChart
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTable
import kotlinx.coroutines.launch
import kotlin.math.floor

@Composable
fun TimelineCanvas(
    state: TimelineState,
    onIntent: (TimelineIntent) -> Unit,
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val colors = LocalColors.current
    val colorScheme = colors.scheme
    val dimensions = LocalDimensions.current
    val typography = AppTheme.typography
    val textMeasurer = rememberTextMeasurer()
    val daysOfWeek = DayOfWeek.entries.associateWith { getString(it.abbreviation) }

    var canvasSize by remember { mutableStateOf(Size.Unspecified) }

    LaunchedEffect(Unit) {
        viewModel.collectEvents { event ->
            when (event) {
                is TimelineEvent.DateSelected -> scope.launch {
                    val daysBetween = state.date.currentDate.daysBetween(event.date)
                    val offset = canvasSize.width * -1 * daysBetween
                    viewModel.scrollOffset.animateTo(offset)
                }
            }
        }
    }

    var coordinates by remember { mutableStateOf<TimelineCoordinates?>(null) }
    val config by remember {
        val config = TimelineConfig(
            daysOfWeek = daysOfWeek,
            padding = density.run { dimensions.padding.P_2.toPx() },
            fontPaint = Paint().apply { color = colorScheme.onBackground },
            fontSize = density.run { typography.bodyMedium.fontSize.toPx() },
            tableRowHeight = density.run { typography.bodyMedium.fontSize.toPx() + dimensions.padding.P_2.toPx() * 2 },
            backgroundColor = colorScheme.background,
            gridStrokeColor = colorScheme.onSurface,
            gridShadowColor = colorScheme.surfaceContainerLowest,
            valueColorNormal = colors.ValueNormal,
            valueColorLow = colors.ValueLow,
            valueColorHigh = colors.ValueHigh,
        )
        mutableStateOf(config)
    }

    LaunchedEffect(viewModel.scrollOffset.value, canvasSize) {
        val widthPerDay = canvasSize.width
        val threshold = (viewModel.scrollOffset.value * -1) + (widthPerDay / 2f)
        val offsetInDays = floor( threshold / widthPerDay)
        val currentDate = state.date.initialDate.plus(offsetInDays.toInt(), DateUnit.DAY)

        onIntent(TimelineIntent.SetCurrentDate(currentDate))

        coordinates = TimelineCoordinates.from(
            size = canvasSize,
            scrollOffset = Offset(x = viewModel.scrollOffset.value, y = 0f),
            tableRowCount = state.table.rowCount,
            config = config,
            property = state.chart.property,
            values = state.chart.values,
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { canvasSize = it.size.toSize() }
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
                            viewModel.scrollOffset.snapTo(viewModel.scrollOffset.value + dragAmount.x)
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            change.consume()
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val velocity = velocityTracker.calculateVelocity()
                            val targetValueX = decay.calculateTargetValue(viewModel.scrollOffset.value, velocity.x)
                            viewModel.scrollOffset.animateTo(
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
        coordinates?.let { coordinates ->
            TimelineChart(state.chart, coordinates, config, textMeasurer)
            TimelineTable(state.table, coordinates, config, textMeasurer)
            TimelineHours(coordinates, config, textMeasurer)
        }
    }
}