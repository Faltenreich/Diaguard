package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.StatisticScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
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
        val navigateTo = { screen: Screen, replace: Boolean ->
            scope.launch {
                if (replace) {
                    navigator.replaceAll(screen)
                } else {
                    navigator.push(screen)
                }
                bottomSheetState.hide()
            }.invokeOnCompletion { onDismissRequest() }
        }
        Column {
            BottomSheetNavigationItem(
                label = MR.strings.dashboard,
                icon = MR.images.ic_dashboard,
                isActive = navigator.lastItem is DashboardScreen,
                onClick = { navigateTo(DashboardScreen, true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.timeline,
                icon = MR.images.ic_timeline,
                isActive = navigator.lastItem is TimelineScreen,
                onClick = { navigateTo(TimelineScreen(), true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.log,
                icon = MR.images.ic_log,
                isActive = navigator.lastItem is LogScreen,
                onClick = { navigateTo(LogScreen(), true) },
            )
            Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
            BottomSheetNavigationItem(
                label = MR.strings.food,
                icon = null,
                isActive = navigator.lastItem is FoodListScreen,
                onClick = { navigateTo(FoodListScreen(), false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.statistic,
                icon = null,
                isActive = navigator.lastItem is StatisticScreen,
                onClick = { navigateTo(StatisticScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.export,
                icon = null,
                isActive = navigator.lastItem is ExportFormScreen,
                onClick = { navigateTo(ExportFormScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.preferences,
                icon = null,
                isActive = navigator.lastItem is PreferenceListScreen,
                onClick = { navigateTo(PreferenceListScreen(), false) },
            )
        }
    }
}