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
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListScreen
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.preferences
import diaguard.shared.generated.resources.statistic
import kotlin.reflect.KClass

@Composable
fun MainMenu(
    currentDestination: String?,
    onItemClick: (screen: Screen, popHistory: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        MainMenuItem(
            label = StartScreen.DASHBOARD.labelResource,
            icon = StartScreen.DASHBOARD.iconResource,
            isSelected = currentDestination.isSelecting(DashboardScreen::class),
            onClick = { onItemClick(DashboardScreen, true) },
        )
        MainMenuItem(
            label = StartScreen.TIMELINE.labelResource,
            icon = StartScreen.TIMELINE.iconResource,
            isSelected = currentDestination.isSelecting(TimelineScreen::class),
            onClick = { onItemClick(TimelineScreen, true) },
        )
        MainMenuItem(
            label = StartScreen.LOG.labelResource,
            icon = StartScreen.LOG.iconResource,
            isSelected = currentDestination.isSelecting(LogScreen::class),
            onClick = { onItemClick(LogScreen, true) },
        )

        Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))

        MainMenuItem(
            label = Res.string.food,
            icon = null,
            isSelected = currentDestination.isSelecting(FoodSearchScreen::class),
            onClick = { onItemClick(FoodSearchScreen(mode = FoodSearchMode.STROLL), false) },
        )
        MainMenuItem(
            label = Res.string.statistic,
            icon = null,
            isSelected = currentDestination.isSelecting(StatisticScreen::class),
            onClick = { onItemClick(StatisticScreen, false) },
        )
        MainMenuItem(
            label = Res.string.export,
            icon = null,
            isSelected = currentDestination.isSelecting(ExportFormScreen::class),
            onClick = { onItemClick(ExportFormScreen, false) },
        )
        MainMenuItem(
            label = Res.string.preferences,
            icon = null,
            isSelected = currentDestination.isSelecting(OverviewPreferenceListScreen::class),
            onClick = { onItemClick(OverviewPreferenceListScreen, false) },
        )
    }
}

private fun String?.isSelecting(kClass: KClass<*>): Boolean {
    val className = kClass.simpleName ?: return false
    return this?.contains(className) ?: false
}