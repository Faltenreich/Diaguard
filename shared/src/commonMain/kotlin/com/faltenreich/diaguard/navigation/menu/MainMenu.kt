package com.faltenreich.diaguard.navigation.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
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
import kotlinx.coroutines.launch

@Composable
fun MainMenu(
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    val scope = rememberCoroutineScope()
    val navigateTo = { screen: Screen, replace: Boolean ->
        scope.launch {
            bottomSheetNavigator.hide()
            if (replace) {
                navigator.replaceAll(screen)
            } else {
                navigator.push(screen)
            }
        }
    }
    Column(modifier = modifier) {
        MainMenuItem(
            label = MR.strings.dashboard,
            icon = MR.images.ic_dashboard,
            isActive = navigator.lastItem is DashboardScreen,
            onClick = { navigateTo(DashboardScreen, true) },
        )
        MainMenuItem(
            label = MR.strings.timeline,
            icon = MR.images.ic_timeline,
            isActive = navigator.lastItem is TimelineScreen,
            onClick = { navigateTo(TimelineScreen(), true) },
        )
        MainMenuItem(
            label = MR.strings.log,
            icon = MR.images.ic_log,
            isActive = navigator.lastItem is LogScreen,
            onClick = { navigateTo(LogScreen(), true) },
        )
        Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
        MainMenuItem(
            label = MR.strings.food,
            icon = null,
            isActive = navigator.lastItem is FoodListScreen,
            onClick = { navigateTo(FoodListScreen(), false) },
        )
        MainMenuItem(
            label = MR.strings.statistic,
            icon = null,
            isActive = navigator.lastItem is StatisticScreen,
            onClick = { navigateTo(StatisticScreen, false) },
        )
        MainMenuItem(
            label = MR.strings.export,
            icon = null,
            isActive = navigator.lastItem is ExportFormScreen,
            onClick = { navigateTo(ExportFormScreen, false) },
        )
        MainMenuItem(
            label = MR.strings.preferences,
            icon = null,
            isActive = navigator.lastItem is PreferenceListScreen,
            onClick = { navigateTo(PreferenceListScreen(), false) },
        )
    }
}