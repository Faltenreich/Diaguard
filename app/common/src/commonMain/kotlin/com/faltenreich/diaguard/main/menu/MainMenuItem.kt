package com.faltenreich.diaguard.main.menu

import com.faltenreich.diaguard.data.navigation.NavigationTarget
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class MainMenuItem(
    val label: StringResource,
    val icon: DrawableResource?,
    val navigationTarget: NavigationTarget,
) {

    DASHBOARD(
        label = Res.string.dashboard,
        icon = Res.drawable.ic_dashboard,
        navigationTarget = NavigationTarget.Dashboard,
    ),
    TIMELINE(
        label = Res.string.timeline,
        icon = Res.drawable.ic_timeline,
        navigationTarget = NavigationTarget.Timeline,
    ),
    LOG(
        label = Res.string.log,
        icon = Res.drawable.ic_log,
        navigationTarget = NavigationTarget.Log,
    ),
    FOOD(
        label = Res.string.food,
        icon = null,
        navigationTarget = NavigationTarget.FoodSearch(mode = NavigationTarget.FoodSearch.Mode.STROLL),
    ),
    STATISTIC(
        label = Res.string.statistic,
        icon = null,
        navigationTarget = NavigationTarget.Statistic,
    ),
    EXPORT(
        label = Res.string.export,
        icon = null,
        navigationTarget = NavigationTarget.ExportForm,
    ),
    PREFERENCES(
        label = Res.string.preferences,
        icon = null,
        navigationTarget = NavigationTarget.OverviewPreferenceList,
    ),
}