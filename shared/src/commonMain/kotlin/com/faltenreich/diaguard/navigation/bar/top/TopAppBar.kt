package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme

@Composable
fun TopAppBar(
    style: TopAppBarStyle,
    navigationIcon: @Composable () -> Unit,
) {
    when (style) {
        is TopAppBarStyle.Hidden -> Unit
        is TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
            title = { style.content() },
            navigationIcon = navigationIcon,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = AppTheme.colors.scheme.primary,
                navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
                titleContentColor = AppTheme.colors.scheme.onPrimary,
                actionIconContentColor = AppTheme.colors.scheme.onPrimary,
            ),
        )
    }
}