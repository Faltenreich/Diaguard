package com.faltenreich.diaguard.shared.view

import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import androidx.compose.material3.ExtendedFloatingActionButton as MaterialExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton as MaterialFloatingActionButton

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MaterialFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = AppTheme.shapes.large,
        containerColor = AppTheme.colors.scheme.onPrimary,
        contentColor = AppTheme.colors.scheme.primary,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
        content = content,
    )
}

@Composable
fun ExtendedFloatingActionButton(
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialExtendedFloatingActionButton(
        text = text,
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        shape = AppTheme.shapes.large,
        containerColor = AppTheme.colors.scheme.onPrimary,
        contentColor = AppTheme.colors.scheme.primary,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
    )
}