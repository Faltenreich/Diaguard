package com.faltenreich.diaguard.log.list.item

import androidx.compose.ui.unit.IntOffset

data class LogDayStickyInfo(
    val state: LogDayState,
    val offset: IntOffset = IntOffset.Zero,
    val clip: Float = 0f,
)