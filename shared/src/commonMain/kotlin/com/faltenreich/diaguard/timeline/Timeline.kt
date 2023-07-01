package com.faltenreich.diaguard.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.chart.TimelineChart
import com.faltenreich.diaguard.timeline.list.TimelineList
import kotlin.math.ceil

@Composable
fun Timeline(
    modifier: Modifier = Modifier,
    viewModel: TimelineViewModel = inject(),
) {
    // TODO: Reset remember when initialDate changes
    var offset by remember { mutableStateOf(Offset.Zero) }
    // FIXME: Merge offset with view state
    val state = viewModel.viewState.collectAsState().value.copy(offset = offset)
    val config = TimelineConfig(
        padding = LocalDensity.current.run { AppTheme.dimensions.padding.P_2.toPx() },
        fontPaint = Paint().apply { color = AppTheme.colors.material.onBackground },
        fontSize = LocalDensity.current.run { AppTheme.typography.bodyMedium.fontSize.toPx() },
        gridStrokeColor = AppTheme.colors.material.onSurfaceVariant,
        gridShadowColor = AppTheme.colors.material.scrim,
        valueColorNormal = AppTheme.colors.Green,
        valueColorLow = AppTheme.colors.Blue,
        valueColorHigh = AppTheme.colors.Red,
    )
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        offset += dragAmount * 1.5f

                        val widthPerDay = size.width
                        val offsetInDays = ceil(offset.x * -1) / widthPerDay
                        val date = state.initialDate.plusDays(offsetInDays.toInt())
                        viewModel.setDate(date)
                    },
                )
            },
    ) {
        TimelineChart(state, config)
        TimelineList(state, config)
    }
}