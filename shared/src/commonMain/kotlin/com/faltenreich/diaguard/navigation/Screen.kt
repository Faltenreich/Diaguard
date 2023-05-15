package com.faltenreich.diaguard.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.entry.form.EntryDeleteBottomAppBarItem
import com.faltenreich.diaguard.entry.form.EntryForm
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.log.Log
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarOwner
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarOwner
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.SearchField
import com.faltenreich.diaguard.timeline.Timeline
import com.faltenreich.diaguard.timeline.TimelineViewModel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

typealias VoyagerScreen = cafe.adriel.voyager.core.screen.Screen

// FIXME: Content must be parcelable to be cached by Voyager
sealed class Screen : VoyagerScreen, TopAppBarOwner, BottomAppBarOwner {

    class Dashboard : Screen() {

        override val bottomAppBarStyle = BottomAppBarStyle.Visible(
            actions = { EntrySearchBottomAppBarItem() },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )

        @Composable
        override fun Content() {
            Dashboard(viewModel = getViewModel())
        }
    }

    class Log(private val date: Date = Date.today()) : Screen() {

        override val bottomAppBarStyle = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
                val viewState = viewModel.viewState.collectAsState().value
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = viewState.date,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )

        @Composable
        override fun Content() {
            Log(viewModel = getViewModel { parametersOf(date) })
        }
    }

    class Timeline(private val date: Date = Date.today()) : Screen() {

        override val bottomAppBarStyle = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
                val viewState = viewModel.viewState.collectAsState().value
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = viewState.date,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )

        @Composable
        override fun Content() {
            Timeline(viewModel = getViewModel { parametersOf(date) })
        }
    }

    class EntryForm(private val entry: Entry? = null) : Screen() {

        override val topAppBarStyle = TopAppBarStyle.CenterAligned {
            val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry) }
            val viewState = viewModel.viewState.collectAsState().value
            val title = if (viewState.isEditing) MR.strings.entry_edit else MR.strings.entry_new
            Text(stringResource(title))
        }

        override val bottomAppBarStyle = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry) }
                val viewState = viewModel.viewState.collectAsState().value
                if (viewState.isEditing) {
                    val navigator = LocalNavigator.currentOrThrow
                    EntryDeleteBottomAppBarItem(onClick = { viewModel.delete(); navigator.pop() })
                }
            },
            floatingActionButton = {
                val viewModel = getViewModel<EntryFormViewModel> { parametersOf(entry) }
                val navigator = LocalNavigator.currentOrThrow
                FloatingActionButton(onClick = { viewModel.submit(); navigator.pop() }) {
                    Icon(Icons.Filled.Check, stringResource(MR.strings.entry_save))
                }
            }
        )

        @Composable
        override fun Content() {
            EntryForm(viewModel = getViewModel { parametersOf(entry) })
        }
    }

    class EntrySearch(private val query: String? = null) : Screen() {

        override val bottomAppBarStyle = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntrySearchViewModel> { parametersOf(query) }
                val state = viewModel.viewState.collectAsState().value
                SearchField(
                    query = state.query,
                    placeholder = stringResource(MR.strings.search_placeholder),
                    onQueryChange = viewModel::onQueryChange,
                )
            }
        )

        @Composable
        override fun Content() {
            EntrySearch(viewModel = getViewModel { parametersOf(query) })
        }
    }
}