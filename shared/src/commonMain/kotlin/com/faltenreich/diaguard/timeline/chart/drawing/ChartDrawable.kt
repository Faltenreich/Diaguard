package com.faltenreich.diaguard.timeline.chart.drawing

import androidx.compose.ui.graphics.drawscope.DrawScope

interface ChartDrawable {

    fun drawOn(drawScope: DrawScope)
}