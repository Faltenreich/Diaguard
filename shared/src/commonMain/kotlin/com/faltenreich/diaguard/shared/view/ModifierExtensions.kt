package com.faltenreich.diaguard.shared.view

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp

fun Modifier.ignoreParentPadding(horizontal: Dp): Modifier {
    return layout { measurable, constraints ->
        val overwrite = constraints.maxWidth + 2 * horizontal.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxWidth = overwrite))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}