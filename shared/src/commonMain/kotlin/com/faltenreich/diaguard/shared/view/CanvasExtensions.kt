package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope

expect fun DrawScope.drawText(
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
)