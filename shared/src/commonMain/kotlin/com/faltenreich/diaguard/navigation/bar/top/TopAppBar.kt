package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.faltenreich.diaguard.AppTheme

@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = AppTheme.colors.scheme.primary,
        navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
        titleContentColor = AppTheme.colors.scheme.onPrimary,
        actionIconContentColor = AppTheme.colors.scheme.onPrimary,
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}

@Deprecated("Replace with overload")
@Composable
fun TopAppBar(
    style: TopAppBarStyle,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .animateContentSize()
            // Prevent animation on x-axis
            .fillMaxWidth(),
    ) {
        when (style) {
            is TopAppBarStyle.Hidden -> Unit
            is TopAppBarStyle.CenterAligned -> TopAppBar(
                title = { style.content() },
                navigationIcon = navigationIcon,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.scheme.primary,
                    navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
                    titleContentColor = AppTheme.colors.scheme.onPrimary,
                    actionIconContentColor = AppTheme.colors.scheme.onPrimary,
                ),
            )
            is TopAppBarStyle.Custom -> style.content()
        }
    }
}