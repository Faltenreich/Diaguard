package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchMode
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceScreen
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
import kotlin.reflect.KClass

@Composable
fun MainMenu(
    currentDestination: String?,
    modifier: Modifier = Modifier,
    viewModel: MainMenuViewModel = inject(),
) {
    val pushScreen = { screen: Screen, popHistory: Boolean ->
        viewModel.dispatchIntent(MainMenuIntent.PushScreen(screen, popHistory))
    }

    Column(modifier = modifier) {
        MainMenuItem(
            label = Res.string.dashboard,
            icon = Res.drawable.ic_dashboard,
            isSelected = currentDestination.isSelecting(DashboardScreen::class),
            onClick = { pushScreen(DashboardScreen, true) },
        )
        MainMenuItem(
            label = Res.string.timeline,
            icon = Res.drawable.ic_timeline,
            isSelected = currentDestination.isSelecting(TimelineScreen::class),
            onClick = { pushScreen(TimelineScreen, true) },
        )
        MainMenuItem(
            label = Res.string.log,
            icon = Res.drawable.ic_log,
            isSelected = currentDestination.isSelecting(LogScreen::class),
            onClick = { pushScreen(LogScreen, true) },
        )

        Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))

        MainMenuItem(
            label = Res.string.food,
            icon = null,
            isSelected = currentDestination.isSelecting(FoodSearchScreen::class),
            onClick = { pushScreen(FoodSearchScreen(mode = FoodSearchMode.STROLL), false) },
        )
        MainMenuItem(
            label = Res.string.statistic,
            icon = null,
            isSelected = currentDestination.isSelecting(StatisticScreen::class),
            onClick = { pushScreen(StatisticScreen, false) },
        )
        MainMenuItem(
            label = Res.string.export,
            icon = null,
            isSelected = currentDestination.isSelecting(ExportFormScreen::class),
            onClick = { pushScreen(ExportFormScreen, false) },
        )
        MainMenuItem(
            label = Res.string.preferences,
            icon = null,
            isSelected = currentDestination.isSelecting(OverviewPreferenceScreen::class),
            onClick = { pushScreen(OverviewPreferenceScreen, false) },
        )
    }
}

private fun String?.isSelecting(kClass: KClass<*>): Boolean {
    val className = kClass.simpleName ?: return false
    return this?.contains(className) ?: false
}