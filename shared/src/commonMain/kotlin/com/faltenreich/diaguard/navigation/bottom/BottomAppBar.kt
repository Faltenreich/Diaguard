package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR

@Composable
fun BottomAppBar(
    style: BottomAppBarStyle,
    onMenuClick: () -> Unit,
) {
    when (style) {
        is BottomAppBarStyle.Hidden -> Unit
        is BottomAppBarStyle.Visible -> {
            androidx.compose.material3.BottomAppBar(
                actions = {
                    BottomAppBarItem(
                        image = Icons.Filled.Menu,
                        contentDescription = MR.strings.menu_open,
                        onClick = onMenuClick,
                    )
                    style.actions()
                },
                floatingActionButton = { style.floatingActionButton() },
            )
        }
    }
}