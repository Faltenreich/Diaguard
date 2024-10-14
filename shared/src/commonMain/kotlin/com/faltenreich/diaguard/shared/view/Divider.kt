@file:Suppress("UnusedReceiverParameter")

package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    orientation: Orientation,
) {
    when (orientation) {
        Orientation.Horizontal -> VerticalDivider(modifier)
        Orientation.Vertical -> HorizontalDivider(modifier)
    }
}

@Composable
fun ColumnScope.Divider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        orientation = Orientation.Vertical,
    )
}

@Composable
fun RowScope.Divider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        orientation = Orientation.Horizontal,
    )
}