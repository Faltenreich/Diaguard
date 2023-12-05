package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.painterResource
import androidx.compose.material3.BottomAppBar as Material3BottomBar

@Composable
fun BottomAppBar(
    style: BottomAppBarStyle,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (style) {
        is BottomAppBarStyle.Hidden -> Box(modifier = modifier)
        is BottomAppBarStyle.Visible -> {
            Material3BottomBar(
                actions = {
                    BottomAppBarItem(
                        painter = painterResource(MR.images.ic_menu),
                        contentDescription = MR.strings.menu_open,
                        onClick = onMenuClick,
                    )
                    style.actions()
                },
                modifier = modifier,
                floatingActionButton = { style.floatingActionButton() },
                containerColor = AppTheme.colors.scheme.primary,
                contentColor = AppTheme.colors.scheme.onPrimary,
            )
        }
    }
}