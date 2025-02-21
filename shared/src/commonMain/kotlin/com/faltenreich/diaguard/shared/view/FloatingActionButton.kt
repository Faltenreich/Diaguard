package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.faltenreich.diaguard.AppTheme
import androidx.compose.material3.ExtendedFloatingActionButton as MaterialExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton as MaterialFloatingActionButton

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.large,
    containerColor: Color = AppTheme.colors.scheme.onPrimary,
    contentColor: Color = AppTheme.colors.scheme.primary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
    content: @Composable () -> Unit,
) {
    MaterialFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        content = content,
    )
}

@Composable
fun ExtendedFloatingActionButton(
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.large,
    containerColor: Color = AppTheme.colors.scheme.onPrimary,
    contentColor: Color = AppTheme.colors.scheme.primary,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
) {
    MaterialExtendedFloatingActionButton(
        text = text,
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
    )
}