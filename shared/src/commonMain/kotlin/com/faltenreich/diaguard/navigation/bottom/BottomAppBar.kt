package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import dev.icerock.moko.resources.compose.painterResource
import androidx.compose.material3.BottomAppBar as Material3BottomBar

@Composable
fun BottomAppBar(
    style: BottomAppBarStyle,
    onMenuClick: () -> Unit,
) {
    when (style) {
        is BottomAppBarStyle.Hidden -> Unit
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
                floatingActionButton = { style.floatingActionButton() },
            )
        }
    }
}