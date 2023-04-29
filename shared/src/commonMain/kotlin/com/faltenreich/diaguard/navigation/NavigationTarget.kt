package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.timeline.Timeline

sealed class NavigationTarget

object DashboardTarget : NavigationTarget(), Screen {
    override val key: ScreenKey
        get() = uniqueScreenKey
    @Composable override fun Content() = Dashboard()
}

object TimelineTarget : NavigationTarget(), Screen {
    @Composable override fun Content() = Timeline()
}

object LogTarget : NavigationTarget(), Screen {
    @Composable override fun Content() = Log()
}

data class EntryFormTarget(val entry: Entry? = null) : NavigationTarget(), Screen {
    // TODO: Remove when recomposition is fixed
    override val key: ScreenKey
        get() = uniqueScreenKey
    @Composable override fun Content() = EntryForm(entry = entry)
}