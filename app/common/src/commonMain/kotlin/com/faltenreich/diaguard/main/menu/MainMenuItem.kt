package com.faltenreich.diaguard.main.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.faltenreich.diaguard.dashboard.DashboardScreen
import com.faltenreich.diaguard.data.navigation.NavigationTarget
import com.faltenreich.diaguard.export.form.ExportFormScreen
import com.faltenreich.diaguard.food.search.FoodSearchScreen
import com.faltenreich.diaguard.log.LogScreen
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListScreen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.dashboard
import com.faltenreich.diaguard.resource.export
import com.faltenreich.diaguard.resource.food
import com.faltenreich.diaguard.resource.ic_dashboard
import com.faltenreich.diaguard.resource.ic_log
import com.faltenreich.diaguard.resource.ic_timeline
import com.faltenreich.diaguard.resource.log
import com.faltenreich.diaguard.resource.preferences
import com.faltenreich.diaguard.resource.statistic
import com.faltenreich.diaguard.resource.timeline
import com.faltenreich.diaguard.statistic.StatisticScreen
import com.faltenreich.diaguard.timeline.TimelineScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

enum class MainMenuItem(
    val label: StringResource,
    val icon: DrawableResource?,
    val navigationTarget: NavigationTarget,
    val screenClass: KClass<*>,
) {

    DASHBOARD(
        label = Res.string.dashboard,
        icon = Res.drawable.ic_dashboard,
        navigationTarget = NavigationTarget.Dashboard,
        screenClass = DashboardScreen::class,
    ),
    TIMELINE(
        label = Res.string.timeline,
        icon = Res.drawable.ic_timeline,
        navigationTarget = NavigationTarget.Timeline,
        screenClass = TimelineScreen::class,
    ),
    LOG(
        label = Res.string.log,
        icon = Res.drawable.ic_log,
        navigationTarget = NavigationTarget.Log,
        screenClass = LogScreen::class,
    ),
    FOOD(
        label = Res.string.food,
        icon = null,
        navigationTarget = NavigationTarget.FoodSearch(mode = NavigationTarget.FoodSearch.Mode.STROLL),
        screenClass = FoodSearchScreen::class, // TODO: and others as well
    ),
    STATISTIC(
        label = Res.string.statistic,
        icon = null,
        navigationTarget = NavigationTarget.Statistic,
        screenClass = StatisticScreen::class,
    ),
    EXPORT(
        label = Res.string.export,
        icon = null,
        navigationTarget = NavigationTarget.ExportForm,
        screenClass = ExportFormScreen::class,
    ),
    PREFERENCES(
        label = Res.string.preferences,
        icon = null,
        navigationTarget = NavigationTarget.OverviewPreferenceList,
        screenClass = OverviewPreferenceListScreen::class,
    ),
    ;

    @Composable
    fun isSelected(navController: NavController): Boolean {
        val currentDestination by navController.currentBackStackEntryAsState()
        val route = currentDestination?.destination?.route ?: return false
        val className = screenClass.simpleName ?: return false
        return route.contains(className)
    }
}