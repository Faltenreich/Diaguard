package com.faltenreich.diaguard.timeline.canvas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.toSize
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.theme.LocalDimensions
import com.faltenreich.diaguard.shared.theme.color.LocalColors
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.faltenreich.diaguard.shared.view.rememberAnimatable
import com.faltenreich.diaguard.timeline.TimelineConfig
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineState
import com.faltenreich.diaguard.timeline.canvas.chart.TimelineChart
import com.faltenreich.diaguard.timeline.canvas.table.TimelineTable
import com.faltenreich.diaguard.timeline.canvas.time.TimelineTime
import com.faltenreich.diaguard.timeline.date.TimelineDateState
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimelineCanvas(
    state: TimelineState,
    scrollOffset: Animatable<Float, AnimationVector1D>,
    onIntent: (TimelineIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val colors = LocalColors.current
    val colorScheme = colors.scheme
    val dimensions = LocalDimensions.current
    val typography = AppTheme.typography
    val fontSize = typography.bodyMedium.fontSize
    val textMeasurer = rememberTextMeasurer()
    val tableRowHeight = density.run {
        typography.bodyMedium.fontSize.toPx() + dimensions.padding.P_2.toPx() * 2
    }
    val statusBarHeight = WindowInsets.statusBars.getTop(density)
    val touchArea = dimensions.size.TouchSizeSmall
    val touchAreaSize = density.run { touchArea.toPx().let { Size(it, it) } }

    val config by remember {
        val config = TimelineConfig(
            padding = density.run { dimensions.padding.P_2.toPx() },
            textStyle = TextStyle(fontSize = fontSize, color = colorScheme.onBackground),
            backgroundColor = colorScheme.background,
            gridStrokeColor = colorScheme.onSurface,
            gridShadowColor = colorScheme.surfaceContainerLowest,
            valueColorNormal = colors.ValueNormal,
            valueColorLow = colors.ValueLow,
            valueColorHigh = colors.ValueHigh,
        )
        mutableStateOf(config)
    }

    LaunchedEffect(scrollOffset.value, onIntent) {
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
                onIntent(
                    TimelineIntent.Setup(
                        canvasSize = coordinates.size.toSize(),
                        tableRowHeight = tableRowHeight,
                        statusBarHeight = statusBarHeight,
                    )
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { position ->
                        onIntent(TimelineIntent.TapCanvas(position, touchAreaSize))
                    },
                )
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
                    },
                )
            },
    ) {
        state.canvas?.run {
            TimelineTime(time, config, textMeasurer)
            TimelineChart(chart, config, textMeasurer)
            TimelineTable(table, config, textMeasurer)
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val date = today()
    TimelineCanvas(
        state = TimelineState(
            date = TimelineDateState(
                initialDate = date,
                currentDate = date,
                currentDateLocalized = date.toString(),
                datePickerDialog = null,
            ),
            canvas = null,
            entryListBottomSheet = null,
        ),
        scrollOffset = rememberAnimatable(),
        onIntent = {},
    )
}