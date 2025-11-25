package com.faltenreich.diaguard.main.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListScreen
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import com.faltenreich.diaguard.view.divider.Divider
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.feature.preference.generated.resources.dashboard
import diaguard.feature.preference.generated.resources.ic_dashboard
import diaguard.feature.preference.generated.resources.ic_log
import diaguard.feature.preference.generated.resources.ic_timeline
import diaguard.feature.preference.generated.resources.log
import diaguard.feature.preference.generated.resources.timeline
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.preferences
import diaguard.shared.generated.resources.statistic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.KClass

@Composable
fun MainMenu(
    currentDestination: String?,
    onItemClick: (target: NavigationTarget, popHistory: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        MainMenuItem(
            label = stringResource(diaguard.feature.preference.generated.resources.Res.string.dashboard),
            icon = painterResource(diaguard.feature.preference.generated.resources.Res.drawable.ic_dashboard),
            isSelected = currentDestination.isSelecting(DashboardScreen::class),
            onClick = { onItemClick(NavigationTarget.Dashboard, true) },
        )
        MainMenuItem(
            label = stringResource(diaguard.feature.preference.generated.resources.Res.string.timeline),
            icon = painterResource(diaguard.feature.preference.generated.resources.Res.drawable.ic_timeline),
            isSelected = currentDestination.isSelecting(TimelineScreen::class),
            onClick = { onItemClick(NavigationTarget.Timeline, true) },
        )
        MainMenuItem(
            label = stringResource(diaguard.feature.preference.generated.resources.Res.string.log),
            icon = painterResource(diaguard.feature.preference.generated.resources.Res.drawable.ic_log),
            isSelected = currentDestination.isSelecting(LogScreen::class),
            onClick = { onItemClick(NavigationTarget.Log, true) },
        )

        Divider(modifier = Modifier.padding(vertical = AppTheme.dimensions.padding.P_2))

        MainMenuItem(
            label = stringResource(Res.string.food),
            icon = null,
            isSelected = currentDestination.isSelecting(FoodSearchScreen::class),
            onClick = { onItemClick(NavigationTarget.FoodSearch(
                mode = NavigationTarget.FoodSearch.Mode.STROLL), false)
            },
        )
        MainMenuItem(
            label = stringResource(Res.string.statistic),
            icon = null,
            isSelected = currentDestination.isSelecting(StatisticScreen::class),
            onClick = { onItemClick(NavigationTarget.Statistic, false) },
        )
        MainMenuItem(
            label = stringResource(Res.string.export),
            icon = null,
            isSelected = currentDestination.isSelecting(ExportFormScreen::class),
            onClick = { onItemClick(NavigationTarget.ExportForm, false) },
        )
        MainMenuItem(
            label = stringResource(Res.string.preferences),
            icon = null,
            isSelected = currentDestination.isSelecting(OverviewPreferenceListScreen::class),
            onClick = { onItemClick(NavigationTarget.OverviewPreferenceList, false) },
        )
    }
}

private fun String?.isSelecting(kClass: KClass<*>): Boolean {
    val className = kClass.simpleName ?: return false
    return this?.contains(className) ?: false
}

@Preview(showBackground = true)
@Composable
private fun Preview() = PreviewScaffold {
    MainMenu(
        currentDestination = null,
        onItemClick = { _, _ -> },
    )
}