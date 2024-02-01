package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.unit.IntOffset

data class StickyHeaderInfo(
    val offset: IntOffset = IntOffset.Zero,
    val clip: Float = 0f,
)