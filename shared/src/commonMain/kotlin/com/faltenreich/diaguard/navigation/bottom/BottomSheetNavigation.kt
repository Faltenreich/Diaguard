package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.launch

@Composable
fun BottomSheetNavigation(
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState,
) {
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.currentOrThrow

    BottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        modifier = modifier,
        sheetState = sheetState,
    ) {
        Column {
            BottomSheetNavigationItem(
                icon = MR.images.ic_dashboard,
                label = MR.strings.dashboard,
                isActive = navigator.lastItem is Screen.Dashboard,
                onClick = {
                    scope.launch { sheetState.hide() }
                    navigator.replaceAll(Screen.Dashboard)
                },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                isActive = navigator.lastItem is Screen.Timeline,
                onClick = {
                    scope.launch { sheetState.hide() }
                    navigator.replaceAll(Screen.Timeline())
                },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                isActive = navigator.lastItem is Screen.Log,
                onClick = {
                    scope.launch { sheetState.hide() }
                    navigator.replaceAll(Screen.Log())
                },
            )
        }
    }
}