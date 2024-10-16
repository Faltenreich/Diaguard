package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.export.ExportFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.overview.PreferenceOverviewScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
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

@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    viewModel: MainMenuViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return
    val currentScreen = state.currentScreen

    val navigateTo = { screen: Screen, popHistory: Boolean ->
        viewModel.dispatchIntent(MainMenuIntent.NavigateTo(screen, popHistory))
    }

    Column(modifier = modifier) {
        MainMenuItem(
            label = Res.string.dashboard,
            icon = Res.drawable.ic_dashboard,
            isActive = currentScreen is DashboardScreen,
            onClick = { navigateTo(DashboardScreen, true) },
        )
        MainMenuItem(
            label = Res.string.timeline,
            icon = Res.drawable.ic_timeline,
            isActive = currentScreen is TimelineScreen,
            onClick = { navigateTo(TimelineScreen, true) },
        )
        MainMenuItem(
            label = Res.string.log,
            icon = Res.drawable.ic_log,
            isActive = currentScreen is LogScreen,
            onClick = { navigateTo(LogScreen, true) },
        )

        Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))

        MainMenuItem(
            label = Res.string.food,
            icon = null,
            isActive = currentScreen is FoodSearchScreen,
            onClick = { navigateTo(FoodSearchScreen(mode = FoodSearchMode.STROLL), false) },
        )
        MainMenuItem(
            label = Res.string.statistic,
            icon = null,
            isActive = currentScreen is StatisticScreen,
            onClick = { navigateTo(StatisticScreen, false) },
        )
        MainMenuItem(
            label = Res.string.export,
            icon = null,
            isActive = currentScreen is ExportFormScreen,
            onClick = { navigateTo(ExportFormScreen, false) },
        )
        MainMenuItem(
            label = Res.string.preferences,
            icon = null,
            isActive = currentScreen is PreferenceOverviewScreen,
            onClick = { navigateTo(PreferenceOverviewScreen, false) },
        )
    }
}