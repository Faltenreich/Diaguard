package com.faltenreich.diaguard.log

import androidx.compose.ui.unit.IntSize
import com.faltenreich.diaguard.log.item.LogDayStickyHeaderInfo

data class LogState(
    val monthHeaderSize: IntSize,
    val dayHeaderSize: IntSize,
    val stickyHeaderInfo: LogDayStickyHeaderInfo,
)