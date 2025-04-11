package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_menu
import diaguard.shared.generated.resources.menu_open
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.BottomAppBar as Material3BottomBar

@Composable
fun BottomAppBar(
    onMenuClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable (() -> Unit)? = null,
    containerColor: Color = AppTheme.colors.scheme.primary,
    contentColor: Color = AppTheme.colors.scheme.onPrimary,
    tonalElevation: Dp = BottomAppBarDefaults.ContainerElevation,
    contentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets,
) {
    Material3BottomBar(
        actions = {
            BottomAppBarItem(
                painter = painterResource(Res.drawable.ic_menu),
                contentDescription = Res.string.menu_open,
                onClick = onMenuClick,
            )
            actions()
        },
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        contentPadding = contentPadding,
        windowInsets = windowInsets,
    )
}

@Deprecated("Replace with overload")
@Composable
fun BottomAppBar(
    style: BottomAppBarStyle,
    onMenuClick: () -> Unit,
) {
    when (style) {
        is BottomAppBarStyle.Visible -> {
            Material3BottomBar(
                actions = {
                    BottomAppBarItem(
                        painter = painterResource(Res.drawable.ic_menu),
                        contentDescription = Res.string.menu_open,
                        onClick = onMenuClick,
                    )
                    style.actions()
                },
                floatingActionButton = { style.floatingActionButton() },
                containerColor = AppTheme.colors.scheme.primary,
                contentColor = AppTheme.colors.scheme.onPrimary,
            )
        }
    }
}