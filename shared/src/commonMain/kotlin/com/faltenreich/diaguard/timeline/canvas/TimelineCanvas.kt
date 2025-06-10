package com.faltenreich.diaguard.timeline.canvas

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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.AppTheme
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    val scrollOffset = rememberSaveable(
        saver = Saver(
            save = { it.value },
            restore = { Animatable(initialValue = it) },
        ),
    ) { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.collectEvents { event ->
            when (event) {
                is TimelineEvent.DateSelected -> scope.launch {
                    val daysBetween = state.date.currentDate.daysBetween(event.date)
                    val offset = viewModel.canvasSize.value.width * -1 * daysBetween
                    scrollOffset.animateTo(offset)
                }
            }
        }
    }

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

    LaunchedEffect(scrollOffset.value) {
        onIntent(
            TimelineIntent.Invalidate(
                scrollOffset = scrollOffset.value,
                state = state,
                config = config,
            )
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                viewModel.canvasSize.update { coordinates.size.toSize() }
            }
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
                            scrollOffset.snapTo(scrollOffset.value + dragAmount.x)
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            change.consume()
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val velocity = velocityTracker.calculateVelocity()
                            val targetValueX = decay.calculateTargetValue(scrollOffset.value, velocity.x)
                            scrollOffset.animateTo(
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
        state.coordinates?.let { coordinates ->
            TimelineChart(state.chart, coordinates, config, textMeasurer)
            TimelineTable(state.table, coordinates, config, textMeasurer)
            TimelineHours(coordinates, config, textMeasurer)
        }
    }
}