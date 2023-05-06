package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import com.faltenreich.diaguard.timeline.Timeline
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
                isActive = navigator.lastItem is Dashboard,
                onClick = { navigator.replaceAll(Dashboard()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                isActive = navigator.lastItem is Timeline,
                onClick = { navigator.replaceAll(Timeline()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                isActive = navigator.lastItem is Log,
                onClick = { navigator.replaceAll(Log()) },
            )
        }
    }
}