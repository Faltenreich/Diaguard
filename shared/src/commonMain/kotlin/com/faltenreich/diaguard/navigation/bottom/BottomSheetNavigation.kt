package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodListScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.StatisticScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.BottomSheetState
import kotlinx.coroutines.launch

@Composable
fun BottomSheetNavigation(
    bottomSheetState: BottomSheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    navigation: Navigation = inject(),
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
                    navigation.replaceAll(screen)
                } else {
                    navigation.push(screen)
                }
                bottomSheetState.hide()
            }.invokeOnCompletion { onDismissRequest() }
        }
        Column {
            BottomSheetNavigationItem(
                label = MR.strings.dashboard,
                icon = MR.images.ic_dashboard,
                isActive = navigation.lastItem is DashboardScreen,
                onClick = { navigateTo(DashboardScreen, true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.timeline,
                icon = MR.images.ic_timeline,
                isActive = navigation.lastItem is TimelineScreen,
                onClick = { navigateTo(TimelineScreen(), true) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.log,
                icon = MR.images.ic_log,
                isActive = navigation.lastItem is LogScreen,
                onClick = { navigateTo(LogScreen(), true) },
            )
            Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
            BottomSheetNavigationItem(
                label = MR.strings.food,
                icon = null,
                isActive = navigation.lastItem is FoodListScreen,
                onClick = { navigateTo(FoodListScreen(), false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.statistic,
                icon = null,
                isActive = navigation.lastItem is StatisticScreen,
                onClick = { navigateTo(StatisticScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.export,
                icon = null,
                isActive = navigation.lastItem is ExportFormScreen,
                onClick = { navigateTo(ExportFormScreen, false) },
            )
            BottomSheetNavigationItem(
                label = MR.strings.preferences,
                icon = null,
                isActive = navigation.lastItem is PreferenceListScreen,
                onClick = { navigateTo(PreferenceListScreen(), false) },
            )
        }
    }
}