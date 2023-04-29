package com.faltenreich.diaguard.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
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
            MenuItem(
                icon = MR.images.ic_dashboard,
                label = MR.strings.dashboard,
                onClick = { navigator.replaceAll(DashboardTarget) },
            )
            MenuItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                onClick = { navigator.replaceAll(TimelineTarget) },
            )
            MenuItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                onClick = { navigator.replaceAll(LogTarget) },
            )
        }
    }
}