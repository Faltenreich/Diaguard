package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.EntryDeleteBottomAppBarItem
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.form.EntryFormViewModel
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.log.LogViewModel
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.view.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.SearchField
import com.faltenreich.diaguard.timeline.TimelineViewModel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

fun Screen.bottomAppBarStyle(): BottomAppBarStyle {
    return when (this) {
        is Screen.Dashboard -> BottomAppBarStyle.Visible(
            actions = { EntrySearchBottomAppBarItem() },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is Screen.Log -> BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
                val currentDate = viewModel.currentDate.collectAsState().value
                EntrySearchBottomAppBarItem()
                DatePickerBottomAppBarItem(
                    date = currentDate,
                    onDatePick = viewModel::setDate,
                )
            },
            floatingActionButton = { EntryFormFloatingActionButton() },
        )
        is Screen.Timeline -> BottomAppBarStyle.Visible(
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
        is Screen.EntryForm -> BottomAppBarStyle.Visible(
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
        is Screen.EntrySearch -> BottomAppBarStyle.Visible(
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
    }
}