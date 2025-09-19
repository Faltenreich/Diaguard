@file:Suppress("UnusedReceiverParameter")

package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Divider(
    orientation: Orientation,
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    when (orientation) {
        Orientation.Horizontal -> VerticalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )
        Orientation.Vertical -> HorizontalDivider(
            modifier = modifier,
            thickness = thickness,
            color = color,
        )
    }
}

@Composable
fun ColumnScope.Divider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    Divider(
        orientation = Orientation.Vertical,
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Composable
fun RowScope.Divider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.color,
) {
    Divider(
        modifier = modifier,
        orientation = Orientation.Horizontal,
        thickness = thickness,
        color = color,
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.size.TouchSizeSmall)) {
        items(count = 10) {
            Divider(orientation = Orientation.Vertical)
        }
    }
}