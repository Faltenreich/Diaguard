package com.faltenreich.diaguard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.timeline.Timeline
import dev.icerock.moko.resources.compose.stringResource

sealed interface NavigationTarget : Screen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()
}

object DashboardTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Dashboard()
    }
}

object TimelineTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Timeline()
    }
}

object LogTarget : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Log(viewModel = rememberScreenModel { LogViewModel(inject()) })
    }
}

data class EntryFormTarget(
    val entry: Entry? = null,
    private val viewModel: EntryFormViewModel = EntryFormViewModel(entry),
) : NavigationTarget {

    override val topAppBarStyle = TopAppBarStyle.CenterAligned {
        Text(stringResource(
            if (entry != null) MR.strings.entry_edit
            else MR.strings.entry_new
        ))
    }

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        floatingActionButton = {
            val navigator = LocalNavigator.currentOrThrow
            FloatingActionButton(
                onClick = {
                    viewModel.submit()
                    navigator.pop()
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Filled.Check, stringResource(MR.strings.entry_save))
            }
        }
    )

    @Composable
    override fun Content() {
        EntryForm(viewModel = rememberScreenModel { viewModel })
    }
}

data class EntrySearchTarget(val query: String? = null) : NavigationTarget {

    @Composable
    override fun Content() {
        EntrySearch(viewModel = rememberScreenModel { EntrySearchViewModel(query, inject(), inject()) })
    }
}