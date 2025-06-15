package com.faltenreich.diaguard.timeline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.faltenreich.diaguard.datetime.Date

sealed interface TimelineIntent {

    data class Setup(
        val canvasSize: Size,
        val tableRowHeight: Float,
        val statusBarHeight: Int,
    ) : TimelineIntent

    data class Invalidate(
        val scrollOffset: Float,
        val state: TimelineState,
        val config: TimelineConfig,
    ) : TimelineIntent

    data class TapCanvas(
        val position: Offset,
        val state: TimelineState,
    ) : TimelineIntent

    data class SelectDate(val date: Date) : TimelineIntent

    data object SelectPreviousDate : TimelineIntent

    data object SelectNextDate : TimelineIntent

    data object CreateEntry : TimelineIntent

    data object SearchEntries : TimelineIntent
}