package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.DashboardTarget
import com.faltenreich.diaguard.navigation.LogTarget
import com.faltenreich.diaguard.navigation.TimelineTarget
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
                onClick = { navigator.replaceAll(DashboardTarget()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                onClick = { navigator.replaceAll(TimelineTarget()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                onClick = { navigator.replaceAll(LogTarget()) },
            )
        }
    }
}