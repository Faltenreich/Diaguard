package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(AppTheme.colors.Material.error),
    )
}