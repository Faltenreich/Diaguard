package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.dashboard.DashboardViewModel
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.Timeline
import com.faltenreich.diaguard.timeline.TimelineViewModel

interface NavigationTarget : Screen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()
}

class DashboardTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Dashboard(viewModel = rememberScreenModel { DashboardViewModel() })
    }
}

class TimelineTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Timeline(viewModel = rememberScreenModel { TimelineViewModel() })
    }
}

class LogTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Log(viewModel = rememberScreenModel { LogViewModel(inject()) })
    }
}

class EntrySearchTarget(private val query: String? = null) : NavigationTarget {

    @Composable
    override fun Content() {
        EntrySearch(viewModel = rememberScreenModel { EntrySearchViewModel(query, inject(), inject()) })
    }
}