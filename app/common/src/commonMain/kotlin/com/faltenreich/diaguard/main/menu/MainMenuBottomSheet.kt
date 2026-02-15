package com.faltenreich.diaguard.main.menu

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.faltenreich.diaguard.data.navigation.NavigationTarget

@Composable
fun MainMenuBottomSheet(
    navController: NavController,
    onDismissRequest: () -> Unit,
    onItemClick: (target: NavigationTarget, popHistory: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        MainMenu(
            currentDestination = navController.currentDestination?.route,
            onItemClick = { target, popHistory ->
                onDismissRequest()
                onItemClick(target, popHistory)
            },
        )
    }
}