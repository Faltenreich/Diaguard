package com.faltenreich.diaguard.timeline.chart

import androidx.compose.ui.geometry.Offset
import com.faltenreich.diaguard.shared.architecture.ViewModel

class TimelineChartViewModel : ViewModel() {

    fun calculateScrollOffset(offset: Offset, dragAmount: Offset): Offset {
        // TODO: Cap y at zero
        // TODO: Change y only if delta is larger than n to prevent accidental scroll
        return offset + dragAmount * SCROLL_FACTOR
    }

    companion object {

        // Accelerate scroll speed a little bit
        private const val SCROLL_FACTOR = 1.5f
    }
}