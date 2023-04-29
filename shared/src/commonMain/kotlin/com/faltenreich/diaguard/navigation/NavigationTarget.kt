package com.faltenreich.diaguard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import dev.icerock.moko.resources.compose.stringResource

sealed class NavigationTarget {

    open val topAppBarStyle: TopAppBarStyle = TopAppBarStyle.Hidden

    open val bottomAppBarStyle: BottomAppBarStyle = BottomAppBarStyle.Hidden
}

object DashboardTarget : NavigationTarget(), Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val navigator = LocalNavigator.currentOrThrow
            BottomAppBarItem(
                image = Icons.Filled.Search,
                contentDescription = MR.strings.search_open,
                onClick = { navigator.push(EntrySearchTarget()) },
            )
        },
        floatingActionButton = {
            val navigator = LocalNavigator.currentOrThrow
            FloatingActionButton(
                onClick = { navigator.push(EntryFormTarget(null)) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Filled.Add, stringResource(MR.strings.entry_new_description))
            }
        }
    )

    @Composable override fun Content() {
        Dashboard()
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

    override val topAppBarStyle = TopAppBarStyle.CenterAligned {
        Text(stringResource(
            if (entry != null) MR.strings.entry_edit
            else MR.strings.entry_new
        ))
    }

    @Composable
    override fun Content() {
        EntryForm(entry = entry)
    }
}

data class EntrySearchTarget(val query: String? = null) : NavigationTarget(), Screen {

    @Composable
    override fun Content() {
        EntrySearch(query = query)
    }
}