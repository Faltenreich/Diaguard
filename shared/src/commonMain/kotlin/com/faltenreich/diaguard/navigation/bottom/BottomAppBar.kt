package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomAppBar(
    style: BottomAppBarStyle,
    sheetState: BottomSheetState,
    scope: CoroutineScope,
) {
    when (style) {
        is BottomAppBarStyle.Hidden -> Unit
        is BottomAppBarStyle.Visible -> {
            androidx.compose.material3.BottomAppBar(
                actions = {
                    BottomAppBarItem(
                        image = Icons.Filled.Menu,
                        contentDescription = MR.strings.menu_open,
                        onClick = { scope.launch { sheetState.show() } },
                    )
                    style.actions()
                },
                floatingActionButton = { style.floatingActionButton() },
            )
        }
    }
}