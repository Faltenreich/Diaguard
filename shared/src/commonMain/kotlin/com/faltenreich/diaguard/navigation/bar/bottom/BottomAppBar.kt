package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_menu
import diaguard.shared.generated.resources.menu_open
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.BottomAppBar as Material3BottomBar

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
                        contentDescription = stringResource(Res.string.menu_open),
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

@Preview
@Composable
private fun Preview() = AppPreview {
    BottomAppBar(
        style = BottomAppBarStyle.Visible(
            actions = {},
            floatingActionButton = {},
        ),
        onMenuClick = {},
    )
}