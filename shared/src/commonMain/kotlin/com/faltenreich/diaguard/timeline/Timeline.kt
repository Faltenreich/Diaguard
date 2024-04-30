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
) {
    val state = viewModel.collectState() ?: return

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val colors = LocalColors.current
    val colorScheme = colors.scheme
    val dimensions = LocalDimensions.current
    val typography = AppTheme.typography
    val textMeasurer = rememberTextMeasurer()
    val daysOfWeek = DayOfWeek.entries.associateWith { getString(it.abbreviation) }

    // TODO: Reset remember when initialDate changes
    val scrollOffset by remember { mutableStateOf(Animatable(0f)) }
    var canvasSize by remember { mutableStateOf(Size.Unspecified) }
    var coordinates by remember { mutableStateOf<TimelineCoordinates?>(null) }
    val config by remember {
        val config = TimelineConfig(
            daysOfWeek = daysOfWeek,
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

    LaunchedEffect(scrollOffset.value) {
        val widthPerDay = canvasSize.width
        val offsetInDays = ceil(scrollOffset.value * -1) / widthPerDay
        val date = state.initialDate.plus(offsetInDays.toInt(), DateUnit.DAY)
        viewModel.dispatchIntent(TimelineIntent.SetDate(date))

        coordinates = TimelineCoordinates.from(
            size = canvasSize,
            scrollOffset = Offset(x = scrollOffset.value, y = 0f),
            listItemCount = state.categoriesForList.size,
            config = config,
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
        coordinates?.let { coordinates ->
            TimelineXAxis(state.initialDate, coordinates, config, textMeasurer)
            TimelineChart(state.initialDate, coordinates, config, state.valuesForChart)
            TimelineYAxis(coordinates, config, textMeasurer)
            TimelineList(coordinates, config, state.categoriesForList, state.valuesForList, textMeasurer)
        }
    }
}