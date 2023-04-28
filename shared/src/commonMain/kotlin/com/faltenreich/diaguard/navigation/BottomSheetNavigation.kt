package com.faltenreich.diaguard.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.launch

@Composable
fun BottomSheetNavigation(
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState,
    viewModel: NavigationViewModel = inject(),
) {
    val scope = rememberCoroutineScope()

    BottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        modifier = modifier,
        sheetState = sheetState,
    ) {
        Column {
            MenuItem(
                icon = MR.images.ic_dashboard,
                label = MR.strings.dashboard,
                onClick = { viewModel.navigate(Screen.Dashboard) },
            )
            MenuItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                onClick = { viewModel.navigate(Screen.Timeline) },
            )
            MenuItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                onClick = { viewModel.navigate(Screen.Log) },
            )
        }
    }
}