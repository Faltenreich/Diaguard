package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.main.MainViewModel
import com.faltenreich.diaguard.navigation.NavigationIntent
import com.faltenreich.diaguard.navigation.screen.DashboardScreen
import com.faltenreich.diaguard.navigation.screen.ExportFormScreen
import com.faltenreich.diaguard.navigation.screen.FoodSearchScreen
import com.faltenreich.diaguard.navigation.screen.LogScreen
import com.faltenreich.diaguard.navigation.screen.PreferenceListScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.StatisticScreen
import com.faltenreich.diaguard.navigation.screen.TimelineScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.Divider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.dashboard
import diaguard.shared.generated.resources.export
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.ic_dashboard
import diaguard.shared.generated.resources.ic_log
import diaguard.shared.generated.resources.ic_timeline
import diaguard.shared.generated.resources.log
import diaguard.shared.generated.resources.preferences
import diaguard.shared.generated.resources.statistic
import diaguard.shared.generated.resources.timeline
import kotlinx.coroutines.launch

@Composable
fun BottomSheetNavigation(
    state: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = inject(),
) {
    BottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = state,
    ) {
        val scope = rememberCoroutineScope()
        val navigateTo = { screen: Screen, clearBackStack: Boolean ->
            scope.launch {
                viewModel.dispatchIntent(NavigationIntent.NavigateTo(screen, clearBackStack))
                state.hide()
            }.invokeOnCompletion { onDismissRequest() }
        }
        Column {
            BottomSheetNavigationItem(
                label = Res.string.dashboard,
                icon = Res.drawable.ic_dashboard,
                isActive = viewModel.getActiveScreen() is DashboardScreen,
                onClick = { navigateTo(DashboardScreen, true) },
            )
            BottomSheetNavigationItem(
                label = Res.string.timeline,
                icon = Res.drawable.ic_timeline,
                isActive = viewModel.getActiveScreen() is TimelineScreen,
                onClick = { navigateTo(TimelineScreen, true) },
            )
            BottomSheetNavigationItem(
                label = Res.string.log,
                icon = Res.drawable.ic_log,
                isActive = viewModel.getActiveScreen() is LogScreen,
                onClick = { navigateTo(LogScreen, true) },
            )
            Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))
            BottomSheetNavigationItem(
                label = Res.string.food,
                icon = null,
                isActive = viewModel.getActiveScreen() is FoodSearchScreen,
                onClick = { navigateTo(FoodSearchScreen(mode = FoodSearchMode.STROLL), false) },
            )
            BottomSheetNavigationItem(
                label = Res.string.statistic,
                icon = null,
                isActive = viewModel.getActiveScreen() is StatisticScreen,
                onClick = { navigateTo(StatisticScreen, false) },
            )
            BottomSheetNavigationItem(
                label = Res.string.export,
                icon = null,
                isActive = viewModel.getActiveScreen() is ExportFormScreen,
                onClick = { navigateTo(ExportFormScreen, false) },
            )
            BottomSheetNavigationItem(
                label = Res.string.preferences,
                icon = null,
                isActive = viewModel.getActiveScreen() is PreferenceListScreen,
                onClick = { navigateTo(PreferenceListScreen(), false) },
            )
        }
    }
}