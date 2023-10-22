package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.launch

@Composable
fun BottomSheetNavigation(
    bottomSheetState: BottomSheetState,
    navigator: Navigator,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = bottomSheetState,
    ) {
        val scope = rememberCoroutineScope()
        val navigateTo: (Screen) -> Unit = { screen ->
            scope.launch {
                navigator.replaceAll(screen)
                bottomSheetState.hide()
            }.invokeOnCompletion {
                onDismissRequest()
            }
        }
        Column {
            BottomSheetNavigationItem(
                label = MR.strings.dashboard,
                icon = MR.images.ic_dashboard,
                isActive = navigator.lastItem is Screen.Dashboard,
                onClick = { navigateTo(Screen.Dashboard) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.timeline,
                icon = MR.images.ic_timeline,
                isActive = navigator.lastItem is Screen.Timeline,
                onClick = { navigateTo(Screen.Timeline()) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.log,
                icon = MR.images.ic_log,
                isActive = navigator.lastItem is Screen.Log,
                onClick = { navigateTo(Screen.Log()) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.export,
                icon = null,
                isActive = navigator.lastItem is Screen.ExportForm,
                onClick = { navigateTo(Screen.ExportForm) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.preferences,
                icon = MR.images.ic_preferences,
                isActive = navigator.lastItem is Screen.PreferenceList,
                onClick = { navigateTo(Screen.PreferenceList()) },
            )
        }
    }
}