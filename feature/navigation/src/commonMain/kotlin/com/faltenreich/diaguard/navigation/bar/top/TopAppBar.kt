package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TopAppBar(
    style: com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle,
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
            is com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.Hidden -> Unit
            is com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned -> CenterAlignedTopAppBar(
                title = { style.content() },
                navigationIcon = navigationIcon,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.scheme.primary,
                    navigationIconContentColor = AppTheme.colors.scheme.onPrimary,
                    titleContentColor = AppTheme.colors.scheme.onPrimary,
                    actionIconContentColor = AppTheme.colors.scheme.onPrimary,
                ),
            )
            is com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.Custom -> style.content()
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBar(
        style = _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(
                "Title"
            )
        },
        navigationIcon = {},
    )
}