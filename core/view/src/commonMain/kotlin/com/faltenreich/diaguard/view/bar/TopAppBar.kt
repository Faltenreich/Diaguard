package com.faltenreich.diaguard.view.bar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            is TopAppBarStyle.Custom -> style.content()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TopAppBar(
        style = TopAppBarStyle.CenterAligned {
            Text(
                "Title"
            )
        },
        navigationIcon = {},
    )
}