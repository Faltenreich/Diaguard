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
                icon = MR.images.ic_dashboard,
                label = MR.strings.dashboard,
                isActive = navigator.lastItem is Screen.Dashboard,
                onClick = { navigateTo(Screen.Dashboard) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_timeline,
                label = MR.strings.timeline,
                isActive = navigator.lastItem is Screen.Timeline,
                onClick = { navigateTo(Screen.Timeline()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_log,
                label = MR.strings.log,
                isActive = navigator.lastItem is Screen.Log,
                onClick = { navigateTo(Screen.Log()) },
            )
            BottomSheetNavigationItem(
                icon = MR.images.ic_preferences,
                label = MR.strings.preferences,
                isActive = navigator.lastItem is Screen.PreferenceList,
                onClick = { navigateTo(Screen.PreferenceList()) },
            )
        }
    }
}