package com.faltenreich.diaguard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.timeline.Timeline

sealed class NavigationTarget {

    @Composable
    open fun BottomAppBarItems() = Unit
}

object DashboardTarget : NavigationTarget(), Screen {

    @Composable override fun Content() {
        Dashboard()
    }

    @Composable
    override fun BottomAppBarItems() {
        val navigator = LocalNavigator.currentOrThrow
        BottomAppBarItem(
            image = Icons.Filled.Search,
            contentDescription = MR.strings.search_open,
            onClick = { navigator.push(EntrySearchTarget) },
        )
    }
}

object TimelineTarget : NavigationTarget(), Screen {

    @Composable
    override fun Content() {
        Timeline()
    }
}

object LogTarget : NavigationTarget(), Screen {

    @Composable
    override fun Content() {
        Log()
    }
}

data class EntryFormTarget(val entry: Entry? = null) : NavigationTarget(), Screen {

    @Composable
    override fun Content() {
        EntryForm(entry = entry)
    }
}

object EntrySearchTarget : NavigationTarget(), Screen {

    @Composable
    override fun Content() {
        EntrySearch()
    }
}